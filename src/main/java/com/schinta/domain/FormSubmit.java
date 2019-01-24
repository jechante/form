package com.schinta.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A FormSubmit.
 */
@Entity
@Table(name = "form_submit")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FormSubmit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 提交的数据
     */
    @NotNull
    @Size(max = 5000)
    @ApiModelProperty(value = "提交的数据", required = true)
    @Column(name = "submit_josn", length = 5000, nullable = false)
    private String submitJosn;

    /**
     * 金数据表单中的字段，代表数据表的主键（有时候后台可以对已有某条数据进行修改或者删除，但本应用场景应该只有新增，即每次用户提交的数据都是一条新的记录）
     */
    @NotNull
    @ApiModelProperty(value = "金数据表单中的字段，代表数据表的主键", required = true)
    @Column(name = "serial_number", nullable = false)
    private Integer serialNumber;

    /**
     * 金数据表单中的字段，创建者
     */
    @Size(max = 64)
    @ApiModelProperty(value = "金数据表单中的字段，创建者")
    @Column(name = "creator_name", length = 64)
    private String creatorName;

    /**
     * 金数据表单中的字段，创建时间
     */
    @ApiModelProperty(value = "金数据表单中的字段，创建时间")
    @Column(name = "created_date_time")
    private LocalDateTime createdDateTime;

    /**
     * 金数据表单中的字段，更新时间
     */
    @ApiModelProperty(value = "金数据表单中的字段，更新时间")
    @Column(name = "updated_date_time")
    private LocalDateTime updatedDateTime;

    /**
     * 金数据表单中的字段，提交人ip
     */
    @Size(max = 20)
    @ApiModelProperty(value = "金数据表单中的字段，提交人ip")
    @Column(name = "info_remote_ip", length = 20)
    private String infoRemoteIp;

    /**
     * 结果是否已处理
     */
    @ApiModelProperty(value = "结果是否已处理")
    @Column(name = "dealflag")
    private Boolean dealflag;

    /**
     * 结果是否已对绩效矩阵进行更新
     */
    @ApiModelProperty(value = "结果是否已对绩效矩阵进行更新")
    @Column(name = "computed")
    private Boolean computed;

    @ManyToOne(fetch = FetchType.LAZY)    @JsonIgnoreProperties("submits")
    private WxUser wxUser;

    @ManyToOne(fetch = FetchType.LAZY)    @JsonIgnoreProperties("submits")
    private BaseForm base;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove


    public Boolean isComputed() {
        return computed;
    }

    public void setComputed(Boolean computed) {
        this.computed = computed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubmitJosn() {
        return submitJosn;
    }

    public FormSubmit submitJosn(String submitJosn) {
        this.submitJosn = submitJosn;
        return this;
    }

    public void setSubmitJosn(String submitJosn) {
        this.submitJosn = submitJosn;
    }

    public Integer getSerialNumber() {
        return serialNumber;
    }

    public FormSubmit serialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
        return this;
    }

    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public FormSubmit creatorName(String creatorName) {
        this.creatorName = creatorName;
        return this;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public FormSubmit createdDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    public void setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public LocalDateTime getUpdatedDateTime() {
        return updatedDateTime;
    }

    public FormSubmit updatedDateTime(LocalDateTime updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
        return this;
    }

    public void setUpdatedDateTime(LocalDateTime updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }

    public String getInfoRemoteIp() {
        return infoRemoteIp;
    }

    public FormSubmit infoRemoteIp(String infoRemoteIp) {
        this.infoRemoteIp = infoRemoteIp;
        return this;
    }

    public void setInfoRemoteIp(String infoRemoteIp) {
        this.infoRemoteIp = infoRemoteIp;
    }

    public Boolean isDealflag() {
        return dealflag;
    }

    public FormSubmit dealflag(Boolean dealflag) {
        this.dealflag = dealflag;
        return this;
    }

    public void setDealflag(Boolean dealflag) {
        this.dealflag = dealflag;
    }

    public WxUser getWxUser() {
        return wxUser;
    }

    public FormSubmit wxUser(WxUser wxUser) {
        this.wxUser = wxUser;
        return this;
    }

    public void setWxUser(WxUser wxUser) {
        this.wxUser = wxUser;
    }

    public BaseForm getBase() {
        return base;
    }

    public FormSubmit base(BaseForm baseForm) {
        this.base = baseForm;
        return this;
    }

    public void setBase(BaseForm baseForm) {
        this.base = baseForm;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FormSubmit formSubmit = (FormSubmit) o;
        if (formSubmit.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), formSubmit.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FormSubmit{" +
            "id=" + getId() +
            ", submitJosn='" + getSubmitJosn() + "'" +
            ", serialNumber=" + getSerialNumber() +
            ", creatorName='" + getCreatorName() + "'" +
            ", createdDateTime='" + getCreatedDateTime() + "'" +
            ", updatedDateTime='" + getUpdatedDateTime() + "'" +
            ", infoRemoteIp='" + getInfoRemoteIp() + "'" +
            ", dealflag='" + isDealflag() + "'" +
            "}";
    }
}
