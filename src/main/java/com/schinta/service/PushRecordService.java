package com.schinta.service;

import com.schinta.config.Constants;
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
import com.schinta.repository.WxUserRepository;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpMassNews;
import me.chanjar.weixin.mp.bean.WxMpMassOpenIdsMessage;
import me.chanjar.weixin.mp.bean.WxMpMassPreviewMessage;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import me.chanjar.weixin.mp.bean.result.WxMpMassSendResult;
import me.chanjar.weixin.mp.bean.result.WxMpMassUploadResult;
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
import java.util.*;
import java.util.stream.Collectors;

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
    private final WxUserRepository wxUserRepository;

    @Autowired
    private EntityManager entityManager;

    private String appid = "wx87d2791c2c3a3ded"; // todo 当用多个公众号时，应该从url中获取（即金数据表单的数据推送url中需要带有appid）

    private WxMpService wxMpService;
    public PushRecordService(PushRecordRepository pushRecordRepository,
                             AlgorithmRepository algorithmRepository,
                             UserMatchRepository userMatchRepository,
                             WxUserRepository wxUserRepository,
                             Map<String, WxMpService> mpServices) {
        this.pushRecordRepository = pushRecordRepository;
        this.algorithmRepository = algorithmRepository;
        this.userMatchRepository = userMatchRepository;
        this.wxUserRepository = wxUserRepository;
        this.wxMpService = mpServices.get(appid);
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

//    /**
//     * Get one pushRecord by id.
//     *
//     * @param id the id of the entity
//     * @return the entity
//     */
//    @Transactional(readOnly = true)
//    public PushRecord findOneWithMatches(Long id) {
//        log.debug("Request to get PushRecord : {}", id);
//        PushRecord pushRecord = pushRecordRepository.findWithMatches(id).get();
////        pushRecord.getUser();
//        return pushRecord;
//    }

    /**
     * Delete the pushRecord by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete PushRecord : {}", id);
        pushRecordRepository.deleteById(id);
    }

    // todo 需要验证用户主动推送数是否达到限制（如每日一次）
    // 计算用户主动请求的此次配对(更新match、新建pushRecord)
    public PushRecord getUserAsked(WxUser wxUser) {
        // 获取默认算法
        Algorithm algorithm = algorithmRepository.findEnabledOneWithEagerRelationships().get();
        // 获取用户与其他所有用户已有的未推送的效用结果
        List<UserMatch> userMatches = userMatchRepository.findUnPushedByWxUserAndAlgorithm(wxUser, algorithm);
        //        if (userMatches.size() == 0) throw new RuntimeException("该用户没有符合的配对");
        PushRecord pushRecord = getUserPushRecord(algorithm, wxUser, userMatches, LocalDateTime.now(), PushType.ASK);

        if (pushRecord != null) {
            entityManager.persist(pushRecord);
        }
        return pushRecord;
    }


    // 计算后台群发的此次配对(更新match、新建pushRecord)
    // todo 已经取消关注的用户之前的配对信息是否还应推送给其他人，目前应该为不被推送。
    public List<PushRecord> getBroadcast(LocalDateTime localDateTime) {
        // 获取默认算法
        Algorithm algorithm = algorithmRepository.findEnabledOneWithEagerRelationships().get();
        // 获取已有活跃用户未被推送的效用结果
        List<UserMatch> allMatches = userMatchRepository.findUnPushedByAlgorithm(algorithm);
        // 生成所有用户
        Set<WxUser> users = new HashSet<>();
        allMatches.forEach(userMatch -> {
            users.add(userMatch.getUserB());
            users.add(userMatch.getUserA());
        });

        List<PushRecord> pushRecords = new ArrayList<>();

        // 生成推送记录
        // 批处理（主要是防止内存溢出）
        int batchSize = 25;
        int i = 0; // set循环的index记录变量
        for (WxUser user : users) {
            if ( i > 0 && i % batchSize == 0 ) {
                //flush a batch of inserts and release memory
                entityManager.flush();
                entityManager.clear();
            }
            // 获取可以被推送给该用户且尚未推送的配对
            List<UserMatch> userMatches = allMatches.stream().filter(userMatch -> {
//                if (userMatch.getUserA().equals(user) && (!userMatch.hasPushedToA())) {
//                    return true;
//                } else if (userMatch.getUserB().equals(user) && (!userMatch.hasPushedToB())) {
//                    return true;
//                } else return false;
                return userMatch.toBePushedToUser(user);
            }).collect(Collectors.toList());
            PushRecord pushRecord = getUserPushRecord(algorithm, user, userMatches, localDateTime, PushType.Regular);
            if (pushRecord != null) { // 不是所有活跃用户都有pushRecord，例如已经主动推送给他了等
                entityManager.persist(pushRecord);
                pushRecords.add(pushRecord);
                i++;
            }
        }

        return pushRecords;
    }

    // 计算某用户的此次配对
    private PushRecord getUserPushRecord(Algorithm algorithm, WxUser wxUser, List<UserMatch> userMatches, LocalDateTime timestamp, PushType pushType) {
        if (userMatches == null || userMatches.size() == 0){ // 如果没有记录则为空
            return null;
        }
        PushRecord pushRecord = new PushRecord();
        pushRecord.setPushDateTime(timestamp);
        pushRecord.setUser(wxUser);
        pushRecord.setPushType(pushType);
        for (int i = 0; i < wxUser.getPushLimit() && i < userMatches.size(); i++) {
            UserMatch userMatch = userMatches.stream().max(Comparator.comparing(UserMatch::getScoreTotal)).get();

//            if(userMatch.getPushStatus() != null && (userMatch.getPushStatus().equals(PushStatus.A) || userMatch.getPushStatus().equals(PushStatus.B))) { // 如果已经被推送给其中一个人（说明已经计算了排名、matchType等）
            if(userMatch.hasPushedToEither()) { // 如果已经被推送给其中任意一个人（说明已经计算了排名、matchType等）
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
        return pushRecord;

    }

    public void wxPush(PushRecord pushRecord) throws WxErrorException, MalformedURLException {
        // 将pushRecord转化为持久态。注意：一定要使用merge的返回值，即pushRecord = 不能漏掉！！！
        if (pushRecord == null)
            return;
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
            url = wxMpService
                .oauth2buildAuthorizationUrl(
                    String.format("%s://%s/wx/redirect/%s/ask-match-result?id=%s", requestURL.getProtocol()/*"http"*/, requestURL.getHost()/*"120acf31.ngrok.io"*/, appid, pushRecord.getId()),
                    WxConsts.OAuth2Scope.SNSAPI_USERINFO, null); // 虽然是用户信息授权，但在公众号内打开效果同静默授权
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
        if (num == 1) {
            article1.setTitle(String.format("已成功与%s完成配对",nickName));
        } else {
            article1.setTitle(String.format("已成功与%s等%s个人完成配对",nickName,num));
        }


//        WxMpKefuMessage.WxArticle article2 = new WxMpKefuMessage.WxArticle();
//        article2.setUrl("URL");
//        article2.setPicUrl("https://www.baidu.com/img/bd_logo1.png?where=super");
//        article2.setDescription("Is Really A Happy Day");
//        article2.setTitle("Happy Day");

        WxMpKefuMessage message = WxMpKefuMessage.NEWS()
            .toUser(Constants.WX_TEST_OPENID) // todo 测试用
//            .toUser(user.getId()) //
            .addArticle(article1)
//            .addArticle(article2)
            .build();
        boolean success = wxMpService.getKefuService().sendKefuMessage(message);
        if (success) {
            log.info("发送客服消息成功");
            pushRecord.setSuccess(true);
        } else {
            log.error("发送客服消息失败");
            pushRecord.setSuccess(false);
        }
    }

    public void wxBroadcast(LocalDateTime localDateTime, List<PushRecord> pushRecords) throws WxErrorException, MalformedURLException {
        WxMpMassUploadResult massUploadResult = this.buildMPNews(localDateTime);

        WxMpMassOpenIdsMessage massMessage = new WxMpMassOpenIdsMessage();
        massMessage.setMsgType(WxConsts.MassMsgType.MPNEWS);
        massMessage.setMediaId(massUploadResult.getMediaId());

        // 不能推送给所有活跃用户，而应该是计算得到的有PushRecord的用户
//        List<String> users = wxUserRepository.getAllActiveUserOpenIds();
        List<String> users = pushRecords.stream().map(PushRecord::getUser).map(WxUser::getId).collect(Collectors.toList());
//        List<String> users = Arrays.asList(new String[] {Constants.WX_TEST_OPENID,Constants.WX_TEST_OPENID}); // todo 测试环境使用（不可用，测试号没有群发权限）
        massMessage.setToUsers(users);

        // 更新pushRecord的状态
        // 批处理（主要是防止内存溢出）
        int batchSize = 25;
        int i = 0; // set循环的index记录变量
        try {
            WxMpMassSendResult massResult = wxMpService.getMassMessageService().massOpenIdsMessageSend(massMessage); // todo 测试群发成功的WxMpMassSendResult包括哪些信息，有什么用
            // 将推送记录标记为推送成功
            for (PushRecord pushRecord : pushRecords) {
                if ( i > 0 && i % batchSize == 0 ) {
                    //flush a batch of inserts and release memory
                    entityManager.flush();
                    entityManager.clear();
                }
                pushRecord.setSuccess(true);
                entityManager.merge(pushRecord);
            }
        } catch (WxErrorException e) {
            e.printStackTrace();
            // 将推送记录标记为推送失败
            for (PushRecord pushRecord : pushRecords) {
                if ( i > 0 && i % batchSize == 0 ) {
                    //flush a batch of inserts and release memory
                    entityManager.flush();
                    entityManager.clear();
                }
                pushRecord.setSuccess(false);
                entityManager.merge(pushRecord);
            }
        }
    }

    // 群发消息预览
    public void wxMassPreview(LocalDateTime localDateTime, String openid) throws MalformedURLException, WxErrorException {
        WxMpMassUploadResult massUploadResult = this.buildMPNews(localDateTime);

        WxMpMassPreviewMessage massMessage = new WxMpMassPreviewMessage();
        massMessage.setMsgType(WxConsts.MassMsgType.MPNEWS);
        massMessage.setMediaId(massUploadResult.getMediaId());
//        massMessage.setToWxUserOpenid(Constants.WX_TEST_OPENID); // todo 测试环境使用
        massMessage.setToWxUserOpenid(openid); // todo 测试环境使用
        WxMpMassSendResult massResult = wxMpService.getMassMessageService().massMessagePreview(massMessage); // todo 测试群发成功的WxMpMassSendResult包括哪些信息，有什么用

    }

    // 构造群发图文消息（主要是正文的链接）
    private WxMpMassUploadResult buildMPNews(LocalDateTime localDateTime) throws WxErrorException, MalformedURLException {
        // 上传图文消息的封面图片。webapp下的静态资源最终会打包到classpath：public下
        WxMediaUploadResult uploadMediaRes = wxMpService.getMaterialService().mediaUpload(WxConsts.MediaFileType.IMAGE, "jpg", getClass().getResourceAsStream("/public/content/images/640.jpg"));

        // 上传图文消息的正文图片(返回的url拼在正文的<img>标签中)
//        WxMediaImgUploadResult imagedMediaRes = wxMpService.getMaterialService().mediaImgUpload(file);
//        String url=imagedMediaRes.getUrl();

        // 测试iframe、js、a标签的支持（经测试均不支持）
//        String testIframe = "<!DOCTYPE html>\n" +
//            "<html>\n" +
//            "\n" +
//            "<head>\n" +
//            "    <title>View Animation</title>\n" +
//            "    <link rel=\"stylesheet\" href=\"https://openlayers.org/en/v5.3.0/css/ol.css\" type=\"text/css\">\n" +
//            "    <!-- The line below is only needed for old environments like Internet Explorer and Android 4.x -->\n" +
//            "    <script src=\"https://cdn.polyfill.io/v2/polyfill.min.js?features=requestAnimationFrame,Element.prototype.classList,URL\"></script>\n" +
//            "</head>\n" +
//            "\n" +
//            "<body>\n" +
//            "    <div id=\"test\">aaaaa</div>\n" +
//            "    <!-- <iframe src=\"http://www.ipmay.com/index.php/articles-detail/156.html\" width=\"100%\" height=\"900\"></iframe> -->\n" +
//            "    <a href=\"http://p.weixin.qq.com/s?__biz=MzU3MjI1MDE2Mw==&amp;mid=2247483871&amp;idx=1&amp;sn=453e1f8421c969b1d64a392bc9e50ea6&amp;chksm=fcd28083cba509957ae16bebb9e646bfc9152cac77bb22ea3fb846ce4a16943a5d695bc3cdce&amp;scene=21#wechat_redirect\" target=\"_blank\" data-itemshowtype=\"0\" data-linktype=\"2\">这是超链接...</a>\n" +
//            "    <script>\n" +
//            "      // document.getElementById('test')\n" +
//            "      var a = document.getElementById('test');\n" +
//            "      a.innerText='bbb';\n" +
//            "    </script>\n" +
//            "  </body>\n" +
//            "</html>";
        // 获取推送结果的url
        String contentSourceUrl = null;
        ServletRequestAttributes servletRequestAttributes =
            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes != null) {
            HttpServletRequest request = servletRequestAttributes.getRequest();
            URL requestURL = new URL(request.getRequestURL().toString());
            contentSourceUrl = wxMpService
                .oauth2buildAuthorizationUrl(
                    String.format("%s://%s/wx/redirect/%s/regular-match-result?timestamp=%s", requestURL.getProtocol(), requestURL.getHost(), appid, localDateTime.toString()),
                    WxConsts.OAuth2Scope.SNSAPI_USERINFO, null); // todo 采用静默授权
        }

        String content = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "</head>\n" +
            "<body>\n" +
            "    <div id=\"test\">由于微信图文无法呈现个性化页面，请点击“阅读原文”查看本次匹配结果</div>\n" +
            "  </body>\n" +
            "</html>";

        WxMpMassNews news = new WxMpMassNews();
        WxMpMassNews.WxMpMassNewsArticle article1 = new WxMpMassNews.WxMpMassNewsArticle();
        article1.setTitle("你的本期新增的配对结果出来了！");
        article1.setContent(content);
        article1.setThumbMediaId(uploadMediaRes.getMediaId());
        article1.setShowCoverPic(true);
        article1.setAuthor("小伊");
        article1.setContentSourceUrl(contentSourceUrl);
        article1.setDigest("看看理想中的他（她）是什么样子的吧！"); // todo 呈现效果需要调试
        news.addArticle(article1);

//        WxMpMassNews.WxMpMassNewsArticle article2 = new WxMpMassNews.WxMpMassNewsArticle();
//        article2.setTitle("标题2");
//        article2.setContent("内容2");
//        article2.setThumbMediaId(uploadMediaRes.getMediaId());
//        article2.setShowCoverPic(true);
//        article2.setAuthor("作者2");
//        article2.setContentSourceUrl("www.baidu.com");
//        article2.setDigest("摘要2");
//        news.addArticle(article2);

        WxMpMassUploadResult massUploadResult = wxMpService.getMassMessageService().massNewsUpload(news);
        return massUploadResult;
    }


}
