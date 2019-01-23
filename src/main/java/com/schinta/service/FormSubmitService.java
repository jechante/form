package com.schinta.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.schinta.domain.*;
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
import java.io.IOException;
import java.lang.reflect.Array;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
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

    // 需要注意的点：需要使用批处理，提高性能 todo 验证批处理是否成功
    public void updateUserPropertyAndDemand(FormSubmit formSubmit) throws IOException {
        ObjectMapper mapper = new ObjectMapper(); //转换器
        Map form = mapper.readValue(formSubmit.getSubmitJosn(), Map.class);
        Map entry = (Map) form.get("entry");

        WxUser user = formSubmit.getWxUser();

//        List<UserProperty> properties = userPropertyRepository.findAllByWxUser(user);
        List<FormField> fields = formFieldRepository.findAllByBaseFormWithBaseProperty(formSubmit.getBase());
        fields.forEach(formField -> {
            Object object = entry.get(formField.getFieldName());
            if (this.hasValue(object)) { // 如果有值
                String value = object.toString();
                BaseProperty baseProperty = formField.getBaseProperty();
//                String value = object == null ? null : object.toString();
                Class propertyClass = PropertyValue.getClassFromFieldType(formField.getFieldType());
                // 这里与一般的增删改查应用的区别在于通过金数据表单提交的属性不知道是新增属性还是修改属性，只能通过id或者NaturalId查询确定，所以这里的查询是不可避免的，事先一次性查出全部加载到内存中不是一个好策略（因为如果是新增属性，不会走缓存，还是会执行通过id查询）
                PropertyValue property = (PropertyValue) entityManager
                    .unwrap(Session.class)
                    .byNaturalId(propertyClass)
                    .using("wxUser", user)
                    .using("base", baseProperty)
                    .load(); // select userdemand_.id as id1_14_ from user_demand userdemand_ where userdemand_.base_id=? and userdemand_.wx_user_id=?
                if (property == null) { // 如果是新增属性
                    try {
                        property = (PropertyValue) propertyClass.newInstance();
                        property.setBase(baseProperty);
                        property.setWxUser(user);
                        property.setPropertyValue(value);
                        // 注意这里的persist一定要放在setWxUser和setPropertyValue之后：原因：1）因为这两个属性被当作了natureId，默认是不可变的(mutable = false)，因此放在前面会报错；
                        // 2）persist之后改变的属性不会一次性insert，而是通过额外的update语句更新，例如下面的setRemark会导致update user_property set property_value=?, remark=? where id=?
                        entityManager.persist(property); // insert into user_demand (base_id, property_value, remark, wx_user_id) values (?, ?, ?, ?)
//                        property.setRemark("测试persist执行顺序"); // 会导致update user_property set property_value=?, remark=? where id=?
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                } else { // 如果是已有属性
                    property.setPropertyValue(value);
                }
            }
        });
        // 处理完成后将formSubmit状态改为已处理
//        entityManager.persist(formSubmit); // 会报错，formSubmit状态为detached，不能使用persist方法
        formSubmit.setDealflag(true);
        formSubmit = this.save(formSubmit); // 如果要将setter方法放在save方法后面执行，必须要formSubmit = xx；如果setter放前面，可以不需要formSubmit =
//        this.save(formSubmit); // 如果没有，之后执行的setter方法不会转化成update
//        formSubmit.setDealflag(true);
        return;
    }

    // 判断提交的json中的属性值是否是空值
    private boolean hasValue(Object value) {
        if (value == null) return false;
        if (value instanceof String) {
            if (value.equals("")) {
                return false;
            } else return true;
        } else if (value.getClass().isArray()) { // value instanceof String[] 这里不需要判断数组元素的类型，即是主类型还是某个具体的对象类型或者Object
            if (Array.getLength(value) == 0) {
                return false;
            } else return true;
        } else if (value instanceof ArrayList) {
            if (((ArrayList)value).size() == 0) { // Jackson将数组统一封装成了ArrayList而不是[]
                return false;
            } else return true;
        } else return true;
    }

    // 需要注意的点：1）除了serial_number外，还需根据update_at判断是重复推送还是确实修改了某一条数据（可能是后台修改）；
    public FormSubmit saveSubmitJson(String submitJson) throws IOException {
        ObjectMapper mapper = new ObjectMapper(); //转换器
        Map form = mapper.readValue(submitJson, Map.class);
        Map entry = (Map) form.get("entry");
        Integer serialNum = (Integer) entry.get("serial_number");
        LocalDateTime updateTime = LocalDateTime.ofInstant(Instant.parse((String) entry.get("updated_at")), ZoneId.systemDefault());

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
        formSubmit.setCreatedDateTime(LocalDateTime.ofInstant(Instant.parse((String) entry.get("created_at")), ZoneId.systemDefault()));
        formSubmit.setUpdatedDateTime(updateTime);
        formSubmit.setCreatorName((String) entry.get("creator_name"));
        formSubmit.setInfoRemoteIp((String) entry.get("info_remote_ip"));

        BaseForm baseForm = baseFormRepository.findByFormCode((String) form.get("form")).orElse(null);
        formSubmit.setBase(baseForm);

//        String openid = (String)entry.get("x_field_weixin_openid"); // todo 生成环境启用（测试阶段，由于金数据无法配置微信测试号来收集用户信息，因此实际获取的openid并非微信测试号的openid，而是小伊配对中心的openid）
        String openid = "oPhnp5scZ4Mf0b9hObV6vj7FqfeA"; // 开发环境启用，为微信测试号的openid
        WxUser user = wxUserRepository.findById(openid).orElse(null); // todo orElseGet （如果可以向未关注用户推送匹配结果，也可以在这里创建用户）
        formSubmit.setWxUser(user);
        formSubmit = this.save(formSubmit);
        return formSubmit;
    }
}
