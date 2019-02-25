package com.schinta.service;

import com.hazelcast.util.StringUtil;
import com.schinta.config.Constants;
import com.schinta.domain.WxUser;
import com.schinta.domain.enumeration.UserStatus;
import com.schinta.repository.WxUserRepository;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.result.WxMpUserList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing WxUser.
 */
@Service
@Transactional
public class WxUserService {

    private final Logger log = LoggerFactory.getLogger(WxUserService.class);

    private final WxUserRepository wxUserRepository;

    private String appid = Constants.WX_APPID; // todo 当用多个公众号时，应该从url中获取（即金数据表单的数据推送url中需要带有appid）

    private WxMpService wxMpService;

    @Autowired
    private EntityManager entityManager;

    public WxUserService(WxUserRepository wxUserRepository,
                         Map<String, WxMpService> mpServices) {
        this.wxUserRepository = wxUserRepository;
        this.wxMpService = mpServices.get(appid);
    }

    /**
     * Save a wxUser.
     *
     * @param wxUser the entity to save
     * @return the persisted entity
     */
    public WxUser save(WxUser wxUser) {
        log.debug("Request to save WxUser : {}", wxUser);        return wxUserRepository.save(wxUser);
    }

    /**
     * Get all the wxUsers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<WxUser> findAll(Pageable pageable) {
        log.debug("Request to get all WxUsers");
        return wxUserRepository.findAll(pageable);
    }


    /**
     * Get one wxUser by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<WxUser> findOne(String id) {
        log.debug("Request to get WxUser : {}", id);
        return wxUserRepository.findById(id);
    }

    /**
     * Delete the wxUser by id.
     *
     * @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete WxUser : {}", id);
        wxUserRepository.deleteById(id);
    }

    // todo 批处理验证与优化
    public void syncAllWxUsers() throws WxErrorException {
        // 获取所有用户的openid列表（一次最多10000）
        List<String> openIds = new ArrayList<>();
        WxMpUserList wxUserList = wxMpService.getUserService().userList(null);
        openIds.addAll(wxUserList.getOpenids());
        while (!StringUtil.isNullOrEmpty(wxUserList.getNextOpenid())) { // 如果返回结果有NextOpenid
            wxUserList = wxMpService.getUserService().userList(wxUserList.getNextOpenid());
            openIds.addAll(wxUserList.getOpenids());
        }

        // 两种方法批量同步：方法一
        // 先查出数据库中已有用户，然后更新（好处是可以保留wx_user表中其他地方设置的该用户的非微信相关信息，例如user_status等）
        Map<String, WxUser> wxUserMap = wxUserRepository.findAll().stream().collect(Collectors.toMap(WxUser::getId, wxUser -> wxUser));
        // 从微信后台获取并更新用户信息（一次最多100）
        int userNum = openIds.size();
        for (int i = 0; i < (userNum / 100) + 1; i++) {
            int start = i*100;
            int end = (i+1)*100;
            end =  end > userNum ? userNum : end;
            List<WxMpUser> wxMpUsers = wxMpService.getUserService().userInfoList(openIds.subList(start, end));
            for (WxMpUser wxMpUser: wxMpUsers) {
                WxUser wxUser = wxUserMap.get(wxMpUser.getOpenId());
                // 如果数据库中已经有该user
                if (wxUser != null) {
                    wxUser.refreshWxInfo(wxMpUser);
                } else {
                    wxUser = new WxUser();
                    wxUser.refreshWxInfo(wxMpUser);
                    wxUser.setPushLimit(2); // todo 改为从配置中获取
                    wxUser.setUserStatus(UserStatus.ACTIVE);
                    entityManager.persist(wxUser);
                }
            }
            // 每100个flush一次（包括更新或者插入）
            entityManager.flush();
            entityManager.clear();
        }

        // 方法二
        // 先批量删除数据库中已有数据，然后批量插入（好处是效率应该更高）

    }
}
