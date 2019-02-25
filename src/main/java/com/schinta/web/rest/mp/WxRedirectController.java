package com.schinta.web.rest.mp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.schinta.config.wx.WxMpConfiguration;
import com.schinta.domain.*;
import com.schinta.repository.BasePropertyRepository;
import com.schinta.repository.PushRecordRepository;
import com.schinta.repository.UserDemandRepository;
import com.schinta.repository.UserPropertyRepository;
import com.schinta.service.PushRecordService;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Edward
 */
@Controller
@RequestMapping("/wx/redirect/{appid}")
public class WxRedirectController {

    private final Logger log = LoggerFactory.getLogger(WxRedirectController.class);

    @Autowired
    private PushRecordRepository pushRecordRepository;

    @Autowired
    private UserPropertyRepository userPropertyRepository;

    @Autowired
    private UserDemandRepository userDemandRepository;

    @Autowired
    private BasePropertyRepository basePropertyRepository;

    @Autowired
    private PushRecordService pushRecordService;

    @Autowired
    private Map<String, WxMpService> mpServices;


    @RequestMapping("/greet")
    public String greetUser(@PathVariable String appid, @RequestParam String code, ModelMap map) {

        WxMpService mpService = WxMpConfiguration.getMpServices().get(appid);

        try {
            WxMpOAuth2AccessToken accessToken = mpService.oauth2getAccessToken(code);
            WxMpUser user = mpService.oauth2getUserInfo(accessToken, null);
            map.put("user", user);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }

        return "greet_user";
    }

    // 主动请求，根据pushRecord的id获取配对结果
    @RequestMapping("/ask-match-result")
    // 只读事务
    @Transactional(readOnly = true)
    public String getAskMatchResult(@PathVariable String appid, @RequestParam String code, ModelMap map, @RequestParam Long id) throws WxErrorException {

        WxMpService mpService = mpServices.get(appid);
        WxMpOAuth2AccessToken accessToken = mpService.oauth2getAccessToken(code);
        WxMpUser user = mpService.oauth2getUserInfo(accessToken, null);

        // 判断是否有权限查看该配对结果
        PushRecord pushRecord = pushRecordRepository.findWithMatchesAndUser(id).orElseThrow(() -> new RuntimeException("没有该条记录"));
        if (!pushRecord.getUser().getId().equals(user.getOpenId())) {
            log.error("无权查看该配对结果");
            throw new RuntimeException("该用户无权查看该配对结果,推送id为："+id);
        }

        this.buildModelMapFromPushRecord(pushRecord,map);
        return "match_result";
    }

    // 主动请求结果预览，根据pushRecord的id获取配对结果
    @RequestMapping("/match-result-preview")
    // 只读事务
    @Transactional(readOnly = true)
    public String getAskMatchPreviewResult(@PathVariable String appid, ModelMap map, @RequestParam Long id) {
        // 判断是否有权限查看该配对结果
        PushRecord pushRecord = pushRecordRepository.findWithMatchesAndUser(id).orElseThrow(() -> new RuntimeException("没有该条记录"));
        this.buildModelMapFromPushRecord(pushRecord,map);
        return "match_result";
    }


    // 日常推送，根据pushRecord的id获取配对结果
    @RequestMapping("/regular-match-result")
    // 只读事务
    @Transactional(readOnly = true)
    public String getMatchResult(@PathVariable String appid, @RequestParam String code, ModelMap map, @RequestParam LocalDateTime timestamp) throws WxErrorException {
        // 根据时间戳和用户id获取推送记录
        WxMpService mpService = mpServices.get(appid);
        WxMpOAuth2AccessToken accessToken = mpService.oauth2getAccessToken(code);
        WxMpUser user = mpService.oauth2getUserInfo(accessToken, null);

        PushRecord pushRecord = pushRecordRepository.findAllByUserIdAndPushDateTimeWithMatchesAndUser(user.getOpenId(), timestamp).orElseThrow(() -> new RuntimeException("没有该条记录"));
        this.buildModelMapFromPushRecord(pushRecord,map);
        return "match_result";
    }

    // pushRecord最好已经急加载了user和userMatchList
    private void buildModelMapFromPushRecord(PushRecord pushRecord, ModelMap map) {

        // 构造模板需要用的变量信息
        ObjectMapper mapper = new ObjectMapper();
        map.put("mapper", mapper);
        map.put("pushRecord", pushRecord);
        map.put("user", pushRecord.getUser());

        // 获取匹配结果，并按总分从大到小排序
        List<UserMatch> userMatchList = pushRecord.getUserMatches().stream().sorted(Comparator.comparing(UserMatch::getScoreTotal).reversed()).collect(Collectors.toList());
        map.put("userMatchList", userMatchList);


        // 获取用户属性和需求
        Set<WxUser> userSet = new HashSet<>();
        userMatchList.forEach(userMatch -> {
            userSet.add(userMatch.getUserA());
            userSet.add(userMatch.getUserB());
        });

        List<UserProperty> userProperties = userPropertyRepository.findAllByWxUserIn(userSet);
        List<UserDemand> userDemands = userDemandRepository.findAllByWxUserIn(userSet);

        Set<Long> basePropertySet = new HashSet<>();
        userProperties.forEach(userProperty -> basePropertySet.add(userProperty.getBase().getId()));
        userDemands.forEach(userDemand -> basePropertySet.add(userDemand.getBase().getId()));
        // 获取完全initial的BaseProperty，默认按id排序 todo 增加order字段
        List<BaseProperty> basePropertyList = basePropertyRepository.findAllByIdIn(basePropertySet);

        // 按用户分组，获取BaseProperty为key的Map
        Map<WxUser, Map<BaseProperty, UserProperty>> propertiesMap = userProperties.stream().collect(Collectors.groupingBy(userProperty -> userProperty.getWxUser(),
            Collectors.toMap(UserProperty::getBase, userProperty -> userProperty)));
        Map<WxUser, Map<BaseProperty, UserDemand>> demandsMap = userDemands.stream().collect(Collectors.groupingBy(userDemand -> userDemand.getWxUser(),
            Collectors.toMap(UserDemand::getBase, userDemand -> userDemand)));
        map.put("basePropertyList", basePropertyList); // todo 不用UserMatch结果应该有不同的basePropertyList
        map.put("propertiesMap", propertiesMap);
        map.put("demandsMap", demandsMap);

        // 获取用户照片信息
        basePropertyList.stream().filter(baseProperty -> baseProperty.getPropertyName().equals("头像")).findAny().ifPresent(baseProperty -> {
            Map<WxUser, List<String>> userPicsMap = propertiesMap.entrySet().stream().collect(Collectors.toMap(entry -> entry.getKey(), entry -> {
                UserProperty property = entry.getValue().get(baseProperty);
                List<String> pics = new ArrayList<>();
                if (property != null) { // 如果用户有头像
                    String propertyValue = property.getPropertyValue();
                    try {
                        pics = mapper.readValue(propertyValue, List.class); // 头像是数组
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return pics;
            }));
            map.put("userPicsMap", userPicsMap);
        });


    }


}
