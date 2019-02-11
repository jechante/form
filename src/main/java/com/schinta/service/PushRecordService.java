package com.schinta.service;

import com.schinta.config.wx.WxMpConfiguration;
import com.schinta.domain.Algorithm;
import com.schinta.domain.PushRecord;
import com.schinta.domain.UserMatch;
import com.schinta.domain.WxUser;
import com.schinta.domain.enumeration.MatchType;
import com.schinta.domain.enumeration.PushStatus;
import com.schinta.domain.enumeration.PushType;
import com.schinta.repository.AlgorithmRepository;
import com.schinta.repository.PushRecordRepository;
import com.schinta.repository.UserMatchRepository;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service Implementation for managing PushRecord.
 */
@Service
@Transactional
public class PushRecordService {

    private final Logger log = LoggerFactory.getLogger(PushRecordService.class);

    private final PushRecordRepository pushRecordRepository;
    private final AlgorithmRepository algorithmRepository;
    private final UserMatchRepository userMatchRepository;

    @Autowired
    private EntityManager entityManager;

    private String appid = "wx87d2791c2c3a3ded"; // todo 当用多个公众号时，应该从url中获取（即金数据表单的数据推送url中需要带有appid）

    public PushRecordService(PushRecordRepository pushRecordRepository,
                             AlgorithmRepository algorithmRepository,
                             UserMatchRepository userMatchRepository) {
        this.pushRecordRepository = pushRecordRepository;
        this.algorithmRepository = algorithmRepository;
        this.userMatchRepository = userMatchRepository;

    }

    /**
     * Save a pushRecord.
     *
     * @param pushRecord the entity to save
     * @return the persisted entity
     */
    public PushRecord save(PushRecord pushRecord) {
        log.debug("Request to save PushRecord : {}", pushRecord);        return pushRecordRepository.save(pushRecord);
    }

    /**
     * Get all the pushRecords.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PushRecord> findAll(Pageable pageable) {
        log.debug("Request to get all PushRecords");
        return pushRecordRepository.findAll(pageable);
    }


    /**
     * Get one pushRecord by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<PushRecord> findOne(Long id) {
        log.debug("Request to get PushRecord : {}", id);
        return pushRecordRepository.findById(id);
    }

    /**
     * Get one pushRecord by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public PushRecord findOneWithMatches(Long id) {
        log.debug("Request to get PushRecord : {}", id);
        PushRecord pushRecord = pushRecordRepository.findWithMatches(id).get();
//        pushRecord.getUser();
        return pushRecord;
    }

    /**
     * Delete the pushRecord by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete PushRecord : {}", id);
        pushRecordRepository.deleteById(id);
    }

    // todo 需要验证用户主动推送数是否达到限制
    public PushRecord getUserAsked(WxUser wxUser) {
        // 获取默认算法
        Algorithm algorithm = algorithmRepository.findEnabledOneWithEagerRelationships().get();
        // 获取用户与其他所有用户已有的效用结果
        List<UserMatch> userMatches = userMatchRepository.findUnPushedByWxUserAndAlgorithm(wxUser, algorithm);
        if (userMatches.size() == 0) throw new RuntimeException("该用户没有符合的配对");
        PushRecord pushRecord = new PushRecord();
        pushRecord.setPushDateTime(LocalDateTime.now());
        pushRecord.setUser(wxUser);
        pushRecord.setPushType(PushType.ASK);
        for (int i = 0; i < wxUser.getPushLimit() && i < userMatches.size(); i++) {
            UserMatch userMatch = userMatches.stream().max(Comparator.comparing(UserMatch::getScoreTotal)).get();

            if(userMatch.getPushStatus() != null && (userMatch.getPushStatus().equals(PushStatus.A) || userMatch.getPushStatus().equals(PushStatus.B))) { // 如果已经被推送给其中一个人（说明已经计算了排名、matchType等）
                userMatch.setPushStatus(PushStatus.BOTH);
            } else { // 如果还未推送过
                WxUser theOther = null;
                if (userMatch.getUserA().equals(wxUser)) {
                    userMatch.setPushStatus(PushStatus.A);
                    userMatch.setMatchType(MatchType.ATOB);
                    userMatch.setRankA(i);
                    theOther = userMatch.getUserB();
                } else if (userMatch.getUserB().equals(wxUser)) {
                    userMatch.setPushStatus(PushStatus.B);
                    userMatch.setMatchType(MatchType.BTOA);
                    userMatch.setRankB(i);
                    theOther = userMatch.getUserA();
                }
                // 计算matchType，是否对于双方都是最佳配对
                List<UserMatch> theOtherMatches = userMatchRepository.findUnPushedByWxUserAndAlgorithm(theOther, algorithm);
                for (int j = 0; j < theOther.getPushLimit(); j++) {
                    UserMatch theOherMatch = theOtherMatches.stream().max(Comparator.comparing(UserMatch::getScoreTotal)).get();
                    if (theOherMatch.equals(userMatch)) {
                        userMatch.setMatchType(MatchType.BIDIRECTIONAL);
                        if (userMatch.getUserA().equals(wxUser)) {
                            userMatch.setRankB(j);
                        } else if (userMatch.getUserB().equals(wxUser)) {
                            userMatch.setRankA(j);
                        }
                        break;
                    }
                    theOtherMatches.remove(userMatch);
                }
            }

            pushRecord.addUserMatches(userMatch);
            // 移除以计算剩下中的最大值
            userMatches.remove(userMatch);
        }
        entityManager.persist(pushRecord);
        return pushRecord;
    }


    public void push(PushRecord pushRecord) throws WxErrorException, MalformedURLException {
        // 将pushRecord转化为持久态。注意：一定要使用merge的返回值！！！
        pushRecord = entityManager.merge(pushRecord);
        log.debug(""+entityManager.contains(pushRecord));
        WxMpKefuMessage.WxArticle article1 = new WxMpKefuMessage.WxArticle();

        // 获取推送结果的url
        String url = null;
        ServletRequestAttributes servletRequestAttributes =
            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes != null) {
            HttpServletRequest request = servletRequestAttributes.getRequest();
            URL requestURL = new URL(request.getRequestURL().toString());
            url = WxMpConfiguration.getMpServices().get(appid)
                .oauth2buildAuthorizationUrl(
                    String.format("%s://%s/wx/redirect/%s/match-result?id=%s", /*requestURL.getProtocol()*/"http", /*requestURL.getHost()*/"120acf31.ngrok.io", appid, pushRecord.getId()),
                    WxConsts.OAuth2Scope.SNSAPI_USERINFO, null); // 采用静默授权
        }

        // 获取昵称、人数等
        Set<UserMatch> userMatches = pushRecord.getUserMatches();
        int num = userMatches.size();
        // 得分最高的人的昵称
        UserMatch userMatch = userMatches.stream().max(Comparator.comparing(UserMatch::getScoreTotal)).get();
        // 将userMatch转化为持久态
//        entityManager.merge(userMatch);

        String nickName;
        WxUser user;
        if (userMatch.getUserA().equals(pushRecord.getUser())) {
            user = userMatch.getUserB();
//            entityManager.merge(user);
        } else {
            user = userMatch.getUserA();
//            entityManager.merge(user);
        }
        nickName = user.getWxNickName();
        article1.setUrl(url);
        article1.setPicUrl(user.getWxHeadimgurl());
        article1.setDescription("点击查看你们的配对情况和分数");
        article1.setTitle(String.format("已成功与%s等%s个人完成配对",nickName,num));

//        WxMpKefuMessage.WxArticle article2 = new WxMpKefuMessage.WxArticle();
//        article2.setUrl("URL");
//        article2.setPicUrl("https://www.baidu.com/img/bd_logo1.png?where=super");
//        article2.setDescription("Is Really A Happy Day");
//        article2.setTitle("Happy Day");

        WxMpKefuMessage message = WxMpKefuMessage.NEWS()
            .toUser("oPhnp5scZ4Mf0b9hObV6vj7FqfeA")
            .addArticle(article1)
//            .addArticle(article2)
            .build();
        final WxMpService wxMpService = WxMpConfiguration.getMpServices().get(appid);
        boolean success = wxMpService.getKefuService().sendKefuMessage(message);
        if (success) {
            log.info("发送客服消息成功");
        } else log.error("发送客服消息失败");
    }



}
