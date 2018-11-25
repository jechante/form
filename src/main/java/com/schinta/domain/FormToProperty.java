package com.schinta.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A FormToProperty.
 */
@Entity
@Table(name = "form_to_property")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FormToProperty implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "form_id")
    private Integer formID;

    @Size(max = 64)
    @Column(name = "key_name", length = 64)
    private String keyName;

    @Size(max = 1)
    @Column(name = "key_type", length = 1)
    private String keyType;

    @Size(max = 32)
    @Column(name = "property_id", length = 32)
    private String propertyId;

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

    public FormToProperty formID(Integer formID) {
        this.formID = formID;
        return this;
    }

    public void setFormID(Integer formID) {
        this.formID = formID;
    }

    public String getKeyName() {
        return keyName;
    }

    public FormToProperty keyName(String keyName) {
        this.keyName = keyName;
        return this;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getKeyType() {
        return keyType;
    }

    public FormToProperty keyType(String keyType) {
        this.keyType = keyType;
        return this;
    }

    public void setKeyType(String keyType) {
        this.keyType = keyType;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public FormToProperty propertyId(String propertyId) {
        this.propertyId = propertyId;
        return this;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
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
        FormToProperty formToProperty = (FormToProperty) o;
        if (formToProperty.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), formToProperty.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FormToProperty{" +
            "id=" + getId() +
            ", formID=" + getFormID() +
            ", keyName='" + getKeyName() + "'" +
            ", keyType='" + getKeyType() + "'" +
            ", propertyId='" + getPropertyId() + "'" +
            "}";
    }
}
