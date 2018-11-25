package com.schinta.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
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

    @NotNull
    @Size(max = 32)
    @Column(name = "submit_id", length = 32, nullable = false)
    private String submitID;

    @Size(max = 1)
    @Column(name = "submit_source", length = 1)
    private String submitSource;

    @Column(name = "form_id")
    private Integer formID;

    @Size(max = 128)
    @Column(name = "form_name", length = 128)
    private String formName;

    @Size(max = 5000)
    @Column(name = "submit_josn", length = 5000)
    private String submitJosn;

    @Size(max = 32)
    @Column(name = "user_id", length = 32)
    private String userID;

    @Size(max = 1)
    @Column(name = "register_channel", length = 1)
    private String registerChannel;

    @Column(name = "submit_date")
    private Integer submitDate;

    @Column(name = "submit_time")
    private Integer submitTime;

    @Size(max = 1)
    @Column(name = "dealflag", length = 1)
    private String dealflag;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubmitID() {
        return submitID;
    }

    public FormSubmit submitID(String submitID) {
        this.submitID = submitID;
        return this;
    }

    public void setSubmitID(String submitID) {
        this.submitID = submitID;
    }

    public String getSubmitSource() {
        return submitSource;
    }

    public FormSubmit submitSource(String submitSource) {
        this.submitSource = submitSource;
        return this;
    }

    public void setSubmitSource(String submitSource) {
        this.submitSource = submitSource;
    }

    public Integer getFormID() {
        return formID;
    }

    public FormSubmit formID(Integer formID) {
        this.formID = formID;
        return this;
    }

    public void setFormID(Integer formID) {
        this.formID = formID;
    }

    public String getFormName() {
        return formName;
    }

    public FormSubmit formName(String formName) {
        this.formName = formName;
        return this;
    }

    public void setFormName(String formName) {
        this.formName = formName;
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

    public String getUserID() {
        return userID;
    }

    public FormSubmit userID(String userID) {
        this.userID = userID;
        return this;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getRegisterChannel() {
        return registerChannel;
    }

    public FormSubmit registerChannel(String registerChannel) {
        this.registerChannel = registerChannel;
        return this;
    }

    public void setRegisterChannel(String registerChannel) {
        this.registerChannel = registerChannel;
    }

    public Integer getSubmitDate() {
        return submitDate;
    }

    public FormSubmit submitDate(Integer submitDate) {
        this.submitDate = submitDate;
        return this;
    }

    public void setSubmitDate(Integer submitDate) {
        this.submitDate = submitDate;
    }

    public Integer getSubmitTime() {
        return submitTime;
    }

    public FormSubmit submitTime(Integer submitTime) {
        this.submitTime = submitTime;
        return this;
    }

    public void setSubmitTime(Integer submitTime) {
        this.submitTime = submitTime;
    }

    public String getDealflag() {
        return dealflag;
    }

    public FormSubmit dealflag(String dealflag) {
        this.dealflag = dealflag;
        return this;
    }

    public void setDealflag(String dealflag) {
        this.dealflag = dealflag;
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
            ", submitID='" + getSubmitID() + "'" +
            ", submitSource='" + getSubmitSource() + "'" +
            ", formID=" + getFormID() +
            ", formName='" + getFormName() + "'" +
            ", submitJosn='" + getSubmitJosn() + "'" +
            ", userID='" + getUserID() + "'" +
            ", registerChannel='" + getRegisterChannel() + "'" +
            ", submitDate=" + getSubmitDate() +
            ", submitTime=" + getSubmitTime() +
            ", dealflag='" + getDealflag() + "'" +
            "}";
    }
}
