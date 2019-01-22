package com.schinta.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.schinta.domain.*;
import com.schinta.domain.enumeration.FieldType;
import com.schinta.repository.*;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.awt.print.Book;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service Implementation for managing FormSubmit.
 */
@Service
@Transactional
public class FormSubmitService {

    private final Logger log = LoggerFactory.getLogger(FormSubmitService.class);

    private final FormSubmitRepository formSubmitRepository;
    private final BaseFormRepository baseFormRepository;
    private final WxUserRepository wxUserRepository;
    private final UserPropertyRepository userPropertyRepository;
    private final FormFieldRepository formFieldRepository;

    @Autowired
    private EntityManager entityManager;

    public FormSubmitService(FormSubmitRepository formSubmitRepository,
                             BaseFormRepository baseFormRepository,
                             WxUserRepository wxUserRepository,
                             UserPropertyRepository userPropertyRepository,
                             FormFieldRepository formFieldRepository) {
        this.formSubmitRepository = formSubmitRepository;
        this.baseFormRepository = baseFormRepository;
        this.wxUserRepository = wxUserRepository;
        this.userPropertyRepository = userPropertyRepository;
        this.formFieldRepository = formFieldRepository;
    }

    /**
     * Save a formSubmit.
     *
     * @param formSubmit the entity to save
     * @return the persisted entity
     */
    public FormSubmit save(FormSubmit formSubmit) {
        log.debug("Request to save FormSubmit : {}", formSubmit);

        return formSubmitRepository.save(formSubmit);
    }

    /**
     * Get all the formSubmits.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<FormSubmit> findAll(Pageable pageable) {
        log.debug("Request to get all FormSubmits");
        return formSubmitRepository.findAll(pageable);
    }


    /**
     * Get one formSubmit by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<FormSubmit> findOne(Long id) {
        log.debug("Request to get FormSubmit : {}", id);
        return formSubmitRepository.findById(id);
    }

    /**
     * Delete the formSubmit by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete FormSubmit : {}", id);
        formSubmitRepository.deleteById(id);
    }

    // 需要注意的点：需要使用批处理，提高性能
    public void updateUserPropertyAndDemand(FormSubmit formSubmit) throws IOException {
        ObjectMapper mapper = new ObjectMapper(); //转换器
        Map form = mapper.readValue(formSubmit.getSubmitJosn(), Map.class);
        Map entry = (Map) form.get("entry");

        WxUser user = formSubmit.getWxUser();

        List<UserProperty> properties = userPropertyRepository.findAllByWxUser(user);
        List<FormField> fields = formFieldRepository.findAllByBaseForm(formSubmit.getBase());
        fields.forEach(formField -> {
            BaseProperty baseProperty = formField.getBaseProperty();

//            if (formField.getFieldType().equals(FieldType.PROPERTY)) { // 如果是用户属性

                String value = (String) entry.get(formField.getFieldName());
                PropertyValue property = (PropertyValue) entityManager
                    .unwrap(Session.class)
                    .byNaturalId( PropertyValue.getClassFromFieldType(formField.getFieldType()) )
                    .using("wxUser", user)
                    .using("base", baseProperty)
                    .load();
                if (property == null) {
                    property = new UserProperty();
                    property.setBase(baseProperty);
                    property.setWxUser(user);
                    property.setPropertyValue(value);
                }
                entityManager.persist(property);

//            } else {
//
//            }
        });

    }

    // 需要注意的点：1）除了serial_number外，还需根据update_at判断是重复推送还是确实修改了某一条数据（可能是后台修改）；
    public FormSubmit saveSubmitJson(String submitJson) throws IOException {
        ObjectMapper mapper = new ObjectMapper(); //转换器
        Map form = mapper.readValue(submitJson, Map.class);
        Map entry = (Map) form.get("entry");
        Integer serialNum = (Integer) entry.get("serial_number");
        LocalDateTime updateTime = LocalDateTime.ofInstant(Instant.parse((String)entry.get("updated_at")), ZoneId.systemDefault());

//        FormSubmit old1 = entityManager.createQuery("select form from FormSubmit form",FormSubmit.class)
////            .setParameter("serialNumber",serialNum)
////            .setParameter("updatedDateTime",updateTime)
//            .getSingleResult();

        // 有时候金数据会重复提交同一条数据，如果是重复则不处理（根据serial_number和update_at判断，有时候可能后台修改用户数据）
        boolean isPresent = formSubmitRepository.findBySerialNumberAndUpdatedDateTime(serialNum, updateTime).isPresent();
//        Optional isPresent = formSubmitRepository.findBySerialNumberAndUpdatedDateTime(serialNum, updateTime);
        if (isPresent) return null;
//        FormSubmit old = entityManager.createQuery("select form from FormSubmit form where form.updatedDateTime = :updatedDateTime",FormSubmit.class)
////            .setParameter("serialNumber",serialNum)
//            .setParameter("updatedDateTime",old1.getUpdatedDateTime())
//            .getSingleResult();
        FormSubmit formSubmit = new FormSubmit();
        formSubmit.setSubmitJosn(submitJson);
        formSubmit.setSerialNumber(serialNum);
        formSubmit.setDealflag(false);
        formSubmit.setCreatedDateTime(LocalDateTime.ofInstant(Instant.parse((String)entry.get("created_at")), ZoneId.systemDefault()));
        formSubmit.setUpdatedDateTime(updateTime);
        formSubmit.setCreatorName((String) entry.get("creator_name"));
        formSubmit.setInfoRemoteIp((String) entry.get("info_remote_ip"));

        BaseForm baseForm = baseFormRepository.findByFormCode((String)form.get("form")).orElse(null);
        formSubmit.setBase(baseForm);

//        String openid = (String)entry.get("x_field_weixin_openid"); // todo 生成环境启用（测试阶段，由于金数据无法配置微信测试号来收集用户信息，因此实际获取的openid并非微信测试号的openid，而是小伊配对中心的openid）
        String openid = "oPhnp5scZ4Mf0b9hObV6vj7FqfeA"; // 开发环境启用，为微信测试号的openid
        WxUser user = wxUserRepository.findById(openid).orElse(null); // todo orElseGet （如果可以向未关注用户推送匹配结果，也可以在这里创建用户）
        formSubmit.setWxUser(user);
        formSubmit = this.save(formSubmit);
        return formSubmit;
    }
}
