package com.schinta.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

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

    @Column(name = "form_id")
    private Integer formID;

    @Size(max = 256)
    @Column(name = "form_name", length = 256)
    private String formName;

    @Size(max = 512)
    @Column(name = "form_describe", length = 512)
    private String formDescribe;

    @Size(max = 512)
    @Column(name = "form_web", length = 512)
    private String formWeb;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getFormID() {
        return formID;
    }

    public BaseForm formID(Integer formID) {
        this.formID = formID;
        return this;
    }

    public void setFormID(Integer formID) {
        this.formID = formID;
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
            ", formID=" + getFormID() +
            ", formName='" + getFormName() + "'" +
            ", formDescribe='" + getFormDescribe() + "'" +
            ", formWeb='" + getFormWeb() + "'" +
            "}";
    }
}
