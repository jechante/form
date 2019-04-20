package com.schinta.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.util.StringUtil;
import com.schinta.config.Constants;
import com.schinta.domain.*;
import com.schinta.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.support.RequestHandledEvent;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.lang.reflect.Array;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

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
    private final UserDemandRepository userDemandRepository;
    private final FormFieldRepository formFieldRepository;

    @Autowired
    private EntityManager entityManager;

    public FormSubmitService(FormSubmitRepository formSubmitRepository,
                             BaseFormRepository baseFormRepository,
                             WxUserRepository wxUserRepository,
                             UserPropertyRepository userPropertyRepository,
                             FormFieldRepository formFieldRepository,
                             UserDemandRepository userDemandRepository) {
        this.formSubmitRepository = formSubmitRepository;
        this.baseFormRepository = baseFormRepository;
        this.wxUserRepository = wxUserRepository;
        this.userPropertyRepository = userPropertyRepository;
        this.formFieldRepository = formFieldRepository;
        this.userDemandRepository = userDemandRepository;
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
    // 避免更新或插入（upsert）前的逐条记录查询（先查询出所有结果，然后通过hashCode或者equals等方法判断是否已经有该记录）
    // todo 1）验证批处理是否成功；
    public void upsertUserPropertyAndDemand(FormSubmit formSubmit) throws IOException, IllegalAccessException, InstantiationException {
        ObjectMapper mapper = new ObjectMapper(); //转换器
        Map form = mapper.readValue(formSubmit.getSubmitJosn(), Map.class);
        Map entry = (Map) form.get("entry");

        WxUser user = formSubmit.getWxUser();

        List<UserProperty> properties = userPropertyRepository.findAllByWxUser(user);
        List<UserDemand> demands = userDemandRepository.findAllByWxUser(user);

        List<FormField> fields = formFieldRepository.findAllByBaseFormWithBaseProperty(formSubmit.getBase());
        for (FormField formField : fields) {
            Object object = entry.get(formField.getFieldName());
            if (this.hasValue(object)) { // 如果有值
                String value = mapper.writeValueAsString(object); // 不能使用object.toString();否则无法解析数据
                BaseProperty baseProperty = formField.getBaseProperty();
//                String value = object == null ? null : object.toString();
                Class propertyClass = PropertyValue.getClassFromFieldType(formField.getFieldType());
                // 这里与一般的增删改查应用的区别在于通过金数据表单提交的属性不知道是新增属性还是修改属性，只能通过id或者NaturalId查询确定，所以这里的查询是不可避免的，事先一次性查出全部加载到内存中不是一个好策略（因为如果是新增属性，不会走缓存，还是会执行通过id查询）
//                PropertyValue property = (PropertyValue) entityManager
//                    .unwrap(Session.class)
//                    .byNaturalId(propertyClass)
//                    .using("wxUser", user)
//                    .using("base", baseProperty)
//                    .load(); // select userdemand_.id as id1_14_ from user_demand userdemand_ where userdemand_.base_id=? and userdemand_.wx_user_id=?
//                if (property == null) { // 如果是新增属性
                PropertyValue property = (PropertyValue) propertyClass.newInstance();
                property.setBase(baseProperty);
                property.setWxUser(user);
                if (propertyClass == UserProperty.class) {
                    int index = properties.indexOf(property);
                    if (index != -1) { // 如果已经有该对象
                        property = properties.get(index);
                    }
                } else {
                    int index = demands.indexOf(property);
                    if (index != -1) { // 如果已经有该对象
                        property = demands.get(index);
                    }
                }
                property.setPropertyValue(value);

                // 注意这里的persist一定要放在setWxUser和setPropertyValue之后：原因：1）因为这两个属性被当作了natureId，默认是不可变的(mutable = false)，因此放在前面会报错；
                // 2）persist之后改变的属性不会一次性insert，而是通过额外的update语句更新，例如下面的setRemark会导致update user_property set property_value=?, remark=? where id=?
                // 3）这里用persist和merge都可以，因为property要么是persistent要么是transient，肯定不是detached状态。但是猜测persist更高效一些（不确定，但经过阅读源码，虽然两者均会针对不同态执行不同逻辑，但persist貌似没有复制属性等操作）
//                entityManager.merge(property); // insert into user_demand (base_id, property_value, remark, wx_user_id) values (?, ?, ?, ?)
                entityManager.persist(property); //
//                        property.setRemark("测试persist执行顺序"); // 会导致update user_property set property_value=?, remark=? where id=?


//                } else { // 如果是已有属性
//                    property.setPropertyValue(value);
//                }
            }

            // 新表单可以保存之前填的信息，需要删除未填字段
            // 方法一：如果之前没有则逐条删除；方法二：先批量删除，再批量插入
            // 这里是方法一
            else {
                // 如果已经有该属性，则删除

            }
        }

        // 处理完成后将formSubmit状态改为已处理
//        测试直接用persist方法
//        entityManager.persist(formSubmit); // 会报错，formSubmit状态被认为detached（因为含有id，而且确实为detached的（从上一个已经关闭的持久化上下文中获取的）），因此不能使用persist方法

////        测试persist方法与save方法区别
//        FormSubmit newForm = new FormSubmit();
////        newForm.setId(formSubmit.getId()); // 如果设置id则newForm被认为是detached,无法调用persist方法，但是可以调用this.save方法
//        newForm.setSubmitJosn(formSubmit.getSubmitJosn());
//        newForm.setSerialNumber(formSubmit.getSerialNumber());
////        entityManager.persist(newForm); // 如果设置了id会报错，formSubmit状态为detached，不能使用persist方法
////        this.save(newForm); // 可行
//        entityManager.merge(newForm); // 可行，同this.save(newForm);

        formSubmit.setDealflag(true);
        formSubmit = this.save(formSubmit); // 如果要将setter方法放在save方法后面执行，必须要formSubmit = xx（因为这里的save其实是调用的merge）；如果setter放前面，可以不需要formSubmit =
//        this.save(formSubmit); // 如果没有，之后执行的setter方法不会转化成update
//        formSubmit.setDealflag(true);
        return;
    }

    // 新表单可以保存之前填的信息，需要删除未填字段
    // 方法一：如果之前没有则逐条删除；方法二：先批量删除，再批量插入
    // 这里是方法二
    public void replaceUserPropertyAndDemand(FormSubmit formSubmit) throws IOException, IllegalAccessException, InstantiationException {
        ObjectMapper mapper = new ObjectMapper(); //转换器
        Map form = mapper.readValue(formSubmit.getSubmitJosn(), Map.class);
        Map entry = (Map) form.get("entry");

        WxUser user = formSubmit.getWxUser();

        // 先删除该用户的所有属性和需求
        userPropertyRepository.deleteAllByWxUser(user);
        userDemandRepository.deleteAllByWxUser(user);

        // 再批量插入
        List<FormField> fields = formFieldRepository.findAllByBaseFormWithBaseProperty(formSubmit.getBase());
        for (FormField formField : fields) {
            Object object = entry.get(formField.getFieldName());
            if (this.hasValue(object)) { // 如果有值
                String value = mapper.writeValueAsString(object); // 不能使用object.toString();否则无法解析数据
                BaseProperty baseProperty = formField.getBaseProperty();
//                String value = object == null ? null : object.toString();
                Class propertyClass = PropertyValue.getClassFromFieldType(formField.getFieldType());
                // 这里与一般的增删改查应用的区别在于通过金数据表单提交的属性不知道是新增属性还是修改属性，只能通过id或者NaturalId查询确定，所以这里的查询是不可避免的，事先一次性查出全部加载到内存中不是一个好策略（因为如果是新增属性，不会走缓存，还是会执行通过id查询）
//                PropertyValue property = (PropertyValue) entityManager
//                    .unwrap(Session.class)
//                    .byNaturalId(propertyClass)
//                    .using("wxUser", user)
//                    .using("base", baseProperty)
//                    .load(); // select userdemand_.id as id1_14_ from user_demand userdemand_ where userdemand_.base_id=? and userdemand_.wx_user_id=?
//                if (property == null) { // 如果是新增属性
                PropertyValue property = (PropertyValue) propertyClass.newInstance();
                property.setBase(baseProperty);
                property.setWxUser(user);
                property.setPropertyValue(value);

                entityManager.persist(property); //
            }
        }

        // 处理完成后将formSubmit状态改为已处理
        formSubmit.setDealflag(true);
        formSubmit = this.save(formSubmit); // 如果要将setter方法放在save方法后面执行，必须要formSubmit = xx（因为这里的save其实是调用的merge）；如果setter放前面，可以不需要formSubmit =
        return;
    }


    // 判断提交的json中的属性值是否是空值
    public boolean hasValue(Object value) {
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
            if (((ArrayList) value).size() == 0) { // Jackson将数组统一封装成了ArrayList而不是[]
                return false;
            } else return true;
        } else if (value instanceof HashMap) {
            HashMap map = (HashMap) value;
            if (map.size() == 0) { // Jackson将对象统一封装成了HashMap
                return false;
            }
            else if (map.containsKey("province")) { // 如果是地址
                String province = (String) map.get("province");
                if (StringUtil.isNullOrEmpty(province)) { // 并且未选择（省字段为空即为未填写）
                    return false;
                } else return true;
            }
            else {
                return true;
            }
        } else return true;
    }

//    @EventListener
//    public void handleEvent (RequestHandledEvent e) {
//        System.out.println("-- RequestHandledEvent --");
//        System.out.println(e);
//    }

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
        // todo 如果同时有多个表单，还需要增加formId字段，即Q4qaJD，并根据这个查重
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

        String openid = (String)entry.get("x_field_weixin_openid"); // todo 生产环境启用（测试阶段，由于金数据无法配置微信测试号来收集用户信息，因此实际获取的openid并非微信测试号的openid，而是小伊配对中心的openid）
//        String openid = Constants.WX_TEST_OPENID; // 开发环境启用，为微信测试号的openid。实际小伊中个人的openid为：oMzbs1A71vYPVjOPtWKPXyL2jFHU
        WxUser user = wxUserRepository.findById(openid).orElse(null); // todo orElseGet （如果可以向未关注用户推送匹配结果，也可以在这里创建用户）；目前要求必须关注，因为虽然不关注也获取用户的openId等信息，并可以将所填写的表单信息与openId对应，从而进行匹配计算，但无法将匹配结果发送给用户。
        formSubmit.setWxUser(user);
        formSubmit = this.save(formSubmit);
        return formSubmit;
    }
}
