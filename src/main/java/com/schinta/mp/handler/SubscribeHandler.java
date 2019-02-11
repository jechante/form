package com.schinta.mp.handler;

import com.schinta.domain.BaseForm;
import com.schinta.domain.User;
import com.schinta.domain.WxUser;
import com.schinta.domain.enumeration.Gender;
import com.schinta.domain.enumeration.UserStatus;
import com.schinta.mp.builder.TextBuilder;
import com.schinta.repository.BaseFormRepository;
import com.schinta.repository.WxUserRepository;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutImageMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutNewsMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

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
                                    Map<String, Object> context, WxMpService weixinService,
                                    WxSessionManager sessionManager) throws WxErrorException {

        this.logger.info("新关注用户 OPENID: " + wxMessage.getFromUser());

        // 获取微信用户基本信息
        try {
            WxMpUser userWxInfo = weixinService.getUserService()
                .userInfo(wxMessage.getFromUser(), null);
            if (userWxInfo != null) {
                // TODO 可以添加关注用户到本地数据库
                WxUser wxUser = new WxUser();
                wxUser.setId(userWxInfo.getOpenId());
                if (userWxInfo.getSubscribe()){
                    wxUser.setUserStatus(UserStatus.ACTIVE);
                }
                wxUser.setWxNickName(userWxInfo.getNickname());
                if (userWxInfo.getSexDesc().equals("男")) {
                    wxUser.setGender(Gender.男);
                } else {
                    wxUser.setGender(Gender.女);
                }
                wxUser.setWxLanguage(userWxInfo.getLanguage());
                wxUser.setWxCity(userWxInfo.getCity());
                wxUser.setWxProvince(userWxInfo.getProvince());
                wxUser.setWxCountry(userWxInfo.getCountry());
                wxUser.setRegisterDateTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(userWxInfo.getSubscribeTime()), TimeZone
                    .getDefault().toZoneId()));
                wxUser.setWxHeadimgurl(userWxInfo.getHeadImgUrl());
                wxUser.setPushLimit(2); // 默认两条 todo 应该改为可配置
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
            WxMpXmlOutNewsMessage.Item item = new WxMpXmlOutNewsMessage.Item();
            item.setTitle("完善个人信息开始匹配吧");
            item.setDescription("信息越完整，匹配的另一半越符合预期");
            String url = baseFormRepository.findByEnabled(true).map(baseForm -> baseForm.getFormWeb()).orElse("https://www.baidu.com/");
            item.setUrl(url);
            item.setPicUrl("https://www.baidu.com/img/bd_logo1.png?where=super"); // todo 应该改为可配置
            WxMpXmlOutNewsMessage m = WxMpXmlOutMessage.NEWS().addArticle(item)
                .fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser())
                .build();
//            return new TextBuilder().build("感谢关注", wxMessage, weixinService);
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
