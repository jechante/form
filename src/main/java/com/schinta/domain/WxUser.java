package com.schinta.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.schinta.domain.enumeration.Gender;

import com.schinta.domain.enumeration.UserStatus;

import com.schinta.domain.enumeration.RegisterChannel;

/**
 * A WxUser.
 */
@Entity
@Table(name = "wx_user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class WxUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Size(max = 28)
    @ApiModelProperty(value = "微信openId")
    @Column(name = "id", length = 28)
    private String id;

    /**
     * 微信昵称
     */
    @Size(max = 64)
    @ApiModelProperty(value = "微信昵称")
    @Column(name = "wx_nick_name", length = 64)
    private String wxNickName;

    /**
     * 微信性别
     */
    @ApiModelProperty(value = "微信性别")
    @Enumerated(EnumType.STRING)
    @Column(name = "wx_gender")
    private Gender wxGender;

    /**
     * 微信国家
     */
    @Size(max = 32)
    @ApiModelProperty(value = "微信国家")
    @Column(name = "wx_country", length = 32)
    private String wxCountry;

    /**
     * 微信省
     */
    @Size(max = 32)
    @ApiModelProperty(value = "微信省")
    @Column(name = "wx_province", length = 32)
    private String wxProvince;

    /**
     * 微信市
     */
    @Size(max = 32)
    @ApiModelProperty(value = "微信市")
    @Column(name = "wx_city", length = 32)
    private String wxCity;

    /**
     * 微信头像图片地址
     */
    @Size(max = 250)
    @ApiModelProperty(value = "微信头像图片地址")
    @Column(name = "wx_headimgurl", length = 250)
    private String wxHeadimgurl;

    /**
     * 微信unionid
     */
    @Size(max = 29)
    @ApiModelProperty(value = "微信unionid")
    @Column(name = "wx_unionid", length = 29)
    private String wxUnionid;

    /**
     * 微信语言
     */
    @Size(max = 32)
    @ApiModelProperty(value = "微信语言")
    @Column(name = "wx_language", length = 32)
    private String wxLanguage;

    /**
     * 表单填报性别（部分用户的微信性别可能不是实际性别）
     */
    @ApiModelProperty(value = "表单填报性别（部分用户的微信性别可能不是实际性别）")
    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    /**
     * 用户状态
     */
    @ApiModelProperty(value = "用户状态")
    @Enumerated(EnumType.STRING)
    @Column(name = "user_status")
    private UserStatus userStatus;

    /**
     * 注册渠道
     */
    @ApiModelProperty(value = "注册渠道")
    @Enumerated(EnumType.STRING)
    @Column(name = "register_channel")
    private RegisterChannel registerChannel;

    /**
     * 注册时间
     */
    @ApiModelProperty(value = "注册时间")
    @Column(name = "register_date_time")
    private LocalDateTime registerDateTime;

    /**
     * 每日主动匹配记录数限制，默认为1。特权用户可以上调该限制
     */
    @ApiModelProperty(value = "每日主动匹配记录数限制，默认为1。特权用户可以上调该限制")
    @Column(name = "push_limit")
    private Integer pushLimit;

    /**
     * 微信用户-用户属性值
     */
    @ApiModelProperty(value = "微信用户-用户属性值")
    @OneToMany(mappedBy = "wxUser")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<UserProperty> properties = new HashSet<>();
    /**
     * 微信用户-用户需求值
     */
    @ApiModelProperty(value = "微信用户-用户需求值")
    @OneToMany(mappedBy = "wxUser")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<UserDemand> demands = new HashSet<>();
    /**
     * 微信用户-用户提交表单
     */
    @ApiModelProperty(value = "微信用户-用户提交表单")
    @OneToMany(mappedBy = "wxUser")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<FormSubmit> submits = new HashSet<>();
    /**
     * 微信用户-用户配对绩效结果（位于前）
     */
    @ApiModelProperty(value = "微信用户-用户配对绩效结果（位于前）")
    @OneToMany(mappedBy = "userA")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<UserMatch> aMatches = new HashSet<>();
    /**
     * 微信用户-用户配对绩效结果（位于后）
     */
    @ApiModelProperty(value = "微信用户-用户配对绩效结果（位于后）")
    @OneToMany(mappedBy = "userB")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<UserMatch> bMatches = new HashSet<>();
    /**
     * 微信用户-结果推送
     */
    @ApiModelProperty(value = "微信用户-结果推送")
    @OneToMany(mappedBy = "user")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PushRecord> pushRecords = new HashSet<>();
    @ManyToOne(fetch = FetchType.LAZY)    @JsonIgnoreProperties("wxUsers")
    private Broker broker;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWxNickName() {
        return wxNickName;
    }

    public WxUser wxNickName(String wxNickName) {
        this.wxNickName = wxNickName;
        return this;
    }

    public void setWxNickName(String wxNickName) {
        this.wxNickName = wxNickName;
    }

    public Gender getWxGender() {
        return wxGender;
    }

    public WxUser wxGender(Gender wxGender) {
        this.wxGender = wxGender;
        return this;
    }

    public void setWxGender(Gender wxGender) {
        this.wxGender = wxGender;
    }

    public String getWxCountry() {
        return wxCountry;
    }

    public WxUser wxCountry(String wxCountry) {
        this.wxCountry = wxCountry;
        return this;
    }

    public void setWxCountry(String wxCountry) {
        this.wxCountry = wxCountry;
    }

    public String getWxProvince() {
        return wxProvince;
    }

    public WxUser wxProvince(String wxProvince) {
        this.wxProvince = wxProvince;
        return this;
    }

    public void setWxProvince(String wxProvince) {
        this.wxProvince = wxProvince;
    }

    public String getWxCity() {
        return wxCity;
    }

    public WxUser wxCity(String wxCity) {
        this.wxCity = wxCity;
        return this;
    }

    public void setWxCity(String wxCity) {
        this.wxCity = wxCity;
    }

    public String getWxHeadimgurl() {
        return wxHeadimgurl;
    }

    public WxUser wxHeadimgurl(String wxHeadimgurl) {
        this.wxHeadimgurl = wxHeadimgurl;
        return this;
    }

    public void setWxHeadimgurl(String wxHeadimgurl) {
        this.wxHeadimgurl = wxHeadimgurl;
    }

    public String getWxUnionid() {
        return wxUnionid;
    }

    public WxUser wxUnionid(String wxUnionid) {
        this.wxUnionid = wxUnionid;
        return this;
    }

    public void setWxUnionid(String wxUnionid) {
        this.wxUnionid = wxUnionid;
    }

    public String getWxLanguage() {
        return wxLanguage;
    }

    public WxUser wxLanguage(String wxLanguage) {
        this.wxLanguage = wxLanguage;
        return this;
    }

    public void setWxLanguage(String wxLanguage) {
        this.wxLanguage = wxLanguage;
    }

    public Gender getGender() {
        return gender;
    }

    public WxUser gender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public WxUser userStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
        return this;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public RegisterChannel getRegisterChannel() {
        return registerChannel;
    }

    public WxUser registerChannel(RegisterChannel registerChannel) {
        this.registerChannel = registerChannel;
        return this;
    }

    public void setRegisterChannel(RegisterChannel registerChannel) {
        this.registerChannel = registerChannel;
    }

    public LocalDateTime getRegisterDateTime() {
        return registerDateTime;
    }

    public WxUser registerDateTime(LocalDateTime registerDateTime) {
        this.registerDateTime = registerDateTime;
        return this;
    }

    public void setRegisterDateTime(LocalDateTime registerDateTime) {
        this.registerDateTime = registerDateTime;
    }

    public Integer getPushLimit() {
        return pushLimit;
    }

    public WxUser pushLimit(Integer pushLimit) {
        this.pushLimit = pushLimit;
        return this;
    }

    public void setPushLimit(Integer pushLimit) {
        this.pushLimit = pushLimit;
    }

    public Set<UserProperty> getProperties() {
        return properties;
    }

    public WxUser properties(Set<UserProperty> userProperties) {
        this.properties = userProperties;
        return this;
    }

    public WxUser addProperties(UserProperty userProperty) {
        this.properties.add(userProperty);
        userProperty.setWxUser(this);
        return this;
    }

    public WxUser removeProperties(UserProperty userProperty) {
        this.properties.remove(userProperty);
        userProperty.setWxUser(null);
        return this;
    }

    public void setProperties(Set<UserProperty> userProperties) {
        this.properties = userProperties;
    }

    public Set<UserDemand> getDemands() {
        return demands;
    }

    public WxUser demands(Set<UserDemand> userDemands) {
        this.demands = userDemands;
        return this;
    }

    public WxUser addDemands(UserDemand userDemand) {
        this.demands.add(userDemand);
        userDemand.setWxUser(this);
        return this;
    }

    public WxUser removeDemands(UserDemand userDemand) {
        this.demands.remove(userDemand);
        userDemand.setWxUser(null);
        return this;
    }

    public void setDemands(Set<UserDemand> userDemands) {
        this.demands = userDemands;
    }

    public Set<FormSubmit> getSubmits() {
        return submits;
    }

    public WxUser submits(Set<FormSubmit> formSubmits) {
        this.submits = formSubmits;
        return this;
    }

    public WxUser addSubmits(FormSubmit formSubmit) {
        this.submits.add(formSubmit);
        formSubmit.setWxUser(this);
        return this;
    }

    public WxUser removeSubmits(FormSubmit formSubmit) {
        this.submits.remove(formSubmit);
        formSubmit.setWxUser(null);
        return this;
    }

    public void setSubmits(Set<FormSubmit> formSubmits) {
        this.submits = formSubmits;
    }

    public Set<UserMatch> getAMatches() {
        return aMatches;
    }

    public WxUser aMatches(Set<UserMatch> userMatches) {
        this.aMatches = userMatches;
        return this;
    }

    public WxUser addAMatches(UserMatch userMatch) {
        this.aMatches.add(userMatch);
        userMatch.setUserA(this);
        return this;
    }

    public WxUser removeAMatches(UserMatch userMatch) {
        this.aMatches.remove(userMatch);
        userMatch.setUserA(null);
        return this;
    }

    public void setAMatches(Set<UserMatch> userMatches) {
        this.aMatches = userMatches;
    }

    public Set<UserMatch> getBMatches() {
        return bMatches;
    }

    public WxUser bMatches(Set<UserMatch> userMatches) {
        this.bMatches = userMatches;
        return this;
    }

    public WxUser addBMatches(UserMatch userMatch) {
        this.bMatches.add(userMatch);
        userMatch.setUserB(this);
        return this;
    }

    public WxUser removeBMatches(UserMatch userMatch) {
        this.bMatches.remove(userMatch);
        userMatch.setUserB(null);
        return this;
    }

    public void setBMatches(Set<UserMatch> userMatches) {
        this.bMatches = userMatches;
    }

    public Set<PushRecord> getPushRecords() {
        return pushRecords;
    }

    public WxUser pushRecords(Set<PushRecord> pushRecords) {
        this.pushRecords = pushRecords;
        return this;
    }

    public WxUser addPushRecords(PushRecord pushRecord) {
        this.pushRecords.add(pushRecord);
        pushRecord.setUser(this);
        return this;
    }

    public WxUser removePushRecords(PushRecord pushRecord) {
        this.pushRecords.remove(pushRecord);
        pushRecord.setUser(null);
        return this;
    }

    public void setPushRecords(Set<PushRecord> pushRecords) {
        this.pushRecords = pushRecords;
    }

    public Broker getBroker() {
        return broker;
    }

    public WxUser broker(Broker broker) {
        this.broker = broker;
        return this;
    }

    public void setBroker(Broker broker) {
        this.broker = broker;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
//        if (o == null || getClass() != o.getClass()) { // 默认为必须完全为同一个class类型
        if (o == null || !(o instanceof WxUser)) { // 改为只要为其子类即可（这样代理对象（仅含id）也可以认为与完全加载的对象相等）todo 后续可能会有一些弊端，例如如果该类会作为其他类的父类时
            return false;
        }
        WxUser wxUser = (WxUser) o;
        if (wxUser.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), wxUser.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WxUser{" +
            "id=" + getId() +
            ", wxNickName='" + getWxNickName() + "'" +
            ", wxGender='" + getWxGender() + "'" +
            ", wxCountry='" + getWxCountry() + "'" +
            ", wxProvince='" + getWxProvince() + "'" +
            ", wxCity='" + getWxCity() + "'" +
            ", wxHeadimgurl='" + getWxHeadimgurl() + "'" +
            ", wxUnionid='" + getWxUnionid() + "'" +
            ", wxLanguage='" + getWxLanguage() + "'" +
            ", gender='" + getGender() + "'" +
            ", userStatus='" + getUserStatus() + "'" +
            ", registerChannel='" + getRegisterChannel() + "'" +
            ", registerDateTime='" + getRegisterDateTime() + "'" +
            ", pushLimit=" + getPushLimit() +
            "}";
    }
}
