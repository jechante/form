package com.schinta.mp.handler;

import com.schinta.domain.WxUser;
import com.schinta.domain.enumeration.UserStatus;
import com.schinta.repository.BaseFormRepository;
import com.schinta.repository.WxUserRepository;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutNewsMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.URL;
import java.util.Map;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Component
public class SubscribeHandler extends AbstractHandler {
    @Autowired
    private WxUserRepository wxUserRepository;
    @Autowired
    private BaseFormRepository baseFormRepository;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService wxMpService,
                                    WxSessionManager sessionManager) throws WxErrorException {
        String openId = wxMessage.getFromUser();
        this.logger.info("新关注用户 OPENID: " + openId);

        // 获取微信用户基本信息
        try {
            WxMpUser userWxInfo = wxMpService.getUserService()
                .userInfo(openId, null);
            if (userWxInfo != null) {
                WxUser wxUser = new WxUser();
                wxUser.refreshWxInfo(userWxInfo); // 设置微信相关信息
                wxUser.setPushLimit(2); // 默认两条 todo 应该改为可配置
//                if (userWxInfo.getSubscribe()){ // 这里肯定是true，因此这是关注事件，所以openid一定是关注用户的openid
                wxUser.setUserStatus(UserStatus.ACTIVE);
//                }
                wxUserRepository.save(wxUser);
            }
        } catch (WxErrorException e) {
            if (e.getError().getErrorCode() == 48001) {
                this.logger.info("该公众号没有获取用户信息权限！");
            }
        }


        WxMpXmlOutMessage responseResult = null;
        try {
            responseResult = this.handleSpecial(wxMessage);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }

        if (responseResult != null) {
            return responseResult;
        }

        try {
            // 主动回复的图文消息
            String picUrl = null;
            ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (servletRequestAttributes != null) {
                HttpServletRequest request = servletRequestAttributes.getRequest();
                URL requestURL = new URL(request.getRequestURL().toString());
                picUrl = String.format("%s://%s/content/images/logo-xiaoyi.jpg", requestURL.getProtocol(), requestURL.getHost());
            }

            WxMpXmlOutNewsMessage.Item item = new WxMpXmlOutNewsMessage.Item();
            item.setTitle("开始匹配吧！");
            item.setDescription("");
            String url = baseFormRepository.findByEnabled(true).map(baseForm -> baseForm.getFormWeb()).orElse("https://www.baidu.com/");
            item.setUrl(url);

            item.setPicUrl(picUrl); // todo 应该改为可配置
//            item.setPicUrl("https://www.baidu.com/img/bd_logo1.png?where=super"); // 测试用
            WxMpXmlOutNewsMessage m = WxMpXmlOutMessage.NEWS().addArticle(item)
                .fromUser(wxMessage.getToUser()).toUser(openId)
                .build();
//            return new TextBuilder().build("感谢关注", wxMessage, weixinService);

            // 通过客服消息回复的小伊使用说明（该消息会先送达用户）
            WxMpKefuMessage message = WxMpKefuMessage
                .TEXT()
                .toUser(openId)
                .content("感谢关注小伊配对中心，请点击下方链接填写个人基本信息及对另一半的期望，完成后系统将立刻为你推荐最佳配对对象！同时小伊还将定期为你推送新的最佳配对对象。\n" +
                    "友情提示：信息越完整，你被其他人匹配到的概率越高，同时为你推荐的另一半越符合预期！\n为了保护你的隐私，取消关注后，即表示不再参与配对！")
                .build();
            // 设置消息的内容等信息
            wxMpService.getKefuService().sendKefuMessage(message);
            return m;
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 处理特殊请求，比如如果是扫码进来的，可以做相应处理
     */
    private WxMpXmlOutMessage handleSpecial(WxMpXmlMessage wxMessage)
        throws Exception {
        //TODO
        return null;
    }

}
