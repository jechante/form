package com.schinta.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.schinta.domain.enumeration.FormVendor;

/**
 * A BaseForm.
 */
@Entity
@Table(name = "base_form")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BaseForm implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 金数据等表单的编号
     */
    @NotNull
    @Size(max = 64)
    @ApiModelProperty(value = "金数据等表单的编号", required = true)
    @Column(name = "form_code", length = 64, nullable = false)
    private String formCode;

    /**
     * 金数据等表单的名称
     */
    @Size(max = 256)
    @ApiModelProperty(value = "金数据等表单的名称")
    @Column(name = "form_name", length = 256)
    private String formName;

    /**
     * 金数据等表单的描述
     */
    @Size(max = 512)
    @ApiModelProperty(value = "金数据等表单的描述")
    @Column(name = "form_describe", length = 512)
    private String formDescribe;

    /**
     * 金数据等表单的发布地址
     */
    @Size(max = 512)
    @ApiModelProperty(value = "金数据等表单的发布地址")
    @Column(name = "form_web", length = 512)
    private String formWeb;

    /**
     * 金数据等表单数据结果的第三方推送地址
     */
    @Size(max = 512)
    @ApiModelProperty(value = "金数据等表单数据结果的第三方推送地址")
    @Column(name = "submit_url", length = 512)
    private String submitUrl;

    /**
     * 第三方表单的提供商
     */
    @ApiModelProperty(value = "第三方表单的提供商")
    @Enumerated(EnumType.STRING)
    @Column(name = "vendor")
    private FormVendor vendor;

    /**
     * 表单-用户提交表单
     */
    @ApiModelProperty(value = "表单-用户提交表单")
    @OneToMany(mappedBy = "base")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<FormSubmit> submits = new HashSet<>();
    /**
     * 表单-表单字段
     */
    @ApiModelProperty(value = "表单-表单字段")
    @OneToMany(mappedBy = "baseForm")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<FormField> fields = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFormCode() {
        return formCode;
    }

    public BaseForm formCode(String formCode) {
        this.formCode = formCode;
        return this;
    }

    public void setFormCode(String formCode) {
        this.formCode = formCode;
    }

    public String getFormName() {
        return formName;
    }

    public BaseForm formName(String formName) {
        this.formName = formName;
        return this;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getFormDescribe() {
        return formDescribe;
    }

    public BaseForm formDescribe(String formDescribe) {
        this.formDescribe = formDescribe;
        return this;
    }

    public void setFormDescribe(String formDescribe) {
        this.formDescribe = formDescribe;
    }

    public String getFormWeb() {
        return formWeb;
    }

    public BaseForm formWeb(String formWeb) {
        this.formWeb = formWeb;
        return this;
    }

    public void setFormWeb(String formWeb) {
        this.formWeb = formWeb;
    }

    public String getSubmitUrl() {
        return submitUrl;
    }

    public BaseForm submitUrl(String submitUrl) {
        this.submitUrl = submitUrl;
        return this;
    }

    public void setSubmitUrl(String submitUrl) {
        this.submitUrl = submitUrl;
    }

    public FormVendor getVendor() {
        return vendor;
    }

    public BaseForm vendor(FormVendor vendor) {
        this.vendor = vendor;
        return this;
    }

    public void setVendor(FormVendor vendor) {
        this.vendor = vendor;
    }

    public Set<FormSubmit> getSubmits() {
        return submits;
    }

    public BaseForm submits(Set<FormSubmit> formSubmits) {
        this.submits = formSubmits;
        return this;
    }

    public BaseForm addSubmits(FormSubmit formSubmit) {
        this.submits.add(formSubmit);
        formSubmit.setBase(this);
        return this;
    }

    public BaseForm removeSubmits(FormSubmit formSubmit) {
        this.submits.remove(formSubmit);
        formSubmit.setBase(null);
        return this;
    }

    public void setSubmits(Set<FormSubmit> formSubmits) {
        this.submits = formSubmits;
    }

    public Set<FormField> getFields() {
        return fields;
    }

    public BaseForm fields(Set<FormField> formFields) {
        this.fields = formFields;
        return this;
    }

    public BaseForm addFields(FormField formField) {
        this.fields.add(formField);
        formField.setBaseForm(this);
        return this;
    }

    public BaseForm removeFields(FormField formField) {
        this.fields.remove(formField);
        formField.setBaseForm(null);
        return this;
    }

    public void setFields(Set<FormField> formFields) {
        this.fields = formFields;
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
        BaseForm baseForm = (BaseForm) o;
        if (baseForm.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), baseForm.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BaseForm{" +
            "id=" + getId() +
            ", formCode='" + getFormCode() + "'" +
            ", formName='" + getFormName() + "'" +
            ", formDescribe='" + getFormDescribe() + "'" +
            ", formWeb='" + getFormWeb() + "'" +
            ", submitUrl='" + getSubmitUrl() + "'" +
            ", vendor='" + getVendor() + "'" +
            "}";
    }
}
