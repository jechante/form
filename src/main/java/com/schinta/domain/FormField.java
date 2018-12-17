package com.schinta.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

import com.schinta.domain.enumeration.FieldType;

/**
 * A FormField.
 */
@Entity
@Table(name = "form_field")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FormField implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 变量名称，基本为field_1等
     */
    @NotNull
    @Size(max = 64)
    @ApiModelProperty(value = "变量名称，基本为field_1等", required = true)
    @Column(name = "field_name", length = 64, nullable = false)
    private String fieldName;

    @Enumerated(EnumType.STRING)
    @Column(name = "field_type")
    private FieldType fieldType;

    @ManyToOne    @JsonIgnoreProperties("fields")
    private BaseForm baseForm;

    @ManyToOne    @JsonIgnoreProperties("fields")
    private BaseProperty baseProperty;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFieldName() {
        return fieldName;
    }

    public FormField fieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public FieldType getFieldType() {
        return fieldType;
    }

    public FormField fieldType(FieldType fieldType) {
        this.fieldType = fieldType;
        return this;
    }

    public void setFieldType(FieldType fieldType) {
        this.fieldType = fieldType;
    }

    public BaseForm getBaseForm() {
        return baseForm;
    }

    public FormField baseForm(BaseForm baseForm) {
        this.baseForm = baseForm;
        return this;
    }

    public void setBaseForm(BaseForm baseForm) {
        this.baseForm = baseForm;
    }

    public BaseProperty getBaseProperty() {
        return baseProperty;
    }

    public FormField baseProperty(BaseProperty baseProperty) {
        this.baseProperty = baseProperty;
        return this;
    }

    public void setBaseProperty(BaseProperty baseProperty) {
        this.baseProperty = baseProperty;
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
        FormField formField = (FormField) o;
        if (formField.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), formField.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FormField{" +
            "id=" + getId() +
            ", fieldName='" + getFieldName() + "'" +
            ", fieldType='" + getFieldType() + "'" +
            "}";
    }
}
