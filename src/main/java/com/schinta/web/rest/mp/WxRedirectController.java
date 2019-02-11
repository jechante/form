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
    public String getAskMatchResult(@PathVariable String appid, @RequestParam String code, ModelMap map, @RequestParam Long id) {

//        WxMpService mpService = WxMpConfiguration.getMpServices().get(appid);
//
//        try {
//            WxMpOAuth2AccessToken accessToken = mpService.oauth2getAccessToken(code);
//            WxMpUser user = mpService.oauth2getUserInfo(accessToken, null);
//
//            // 判断是否有权限查看该配对结果
//            PushRecord pushRecord = pushRecordRepository.findById(id).orElseThrow(() -> new RuntimeException("没有该条记录"));
//            if (!pushRecord.getUser().getId().equals(user.getOpenId())) {
//                log.error("无权查看该配对结果");
//            }
//            map.put("user", user);
//        } catch (WxErrorException e) {
//            e.printStackTrace();
//        }
        // 测试模板
        map.put("mapper", new ObjectMapper());

        id = 2l;
        PushRecord pushRecord = pushRecordService.findOneWithMatches(Long.valueOf(2));
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
        Map<WxUser, Map<BaseProperty,UserProperty>> propertiesMap = userProperties.stream().collect(Collectors.groupingBy(userProperty -> userProperty.getWxUser(),
            Collectors.toMap(UserProperty::getBase, userProperty -> userProperty)));
        Map<WxUser, Map<BaseProperty,UserDemand>> demandsMap = userDemands.stream().collect(Collectors.groupingBy(userDemand -> userDemand.getWxUser(),
            Collectors.toMap(UserDemand::getBase, userDemand -> userDemand)));
        map.put("basePropertyList", basePropertyList);
        map.put("propertiesMap", propertiesMap);
        map.put("demandsMap", demandsMap);



        return "match_result";
    }


    // 日常推送，根据pushRecord的id获取配对结果
    @RequestMapping("/regular-match-result")
    public String getMatchResult(@PathVariable String appid, @RequestParam String code, ModelMap map, @RequestParam Long id) {

        return "match_result";
    }
}
