package com.schinta.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A BaseProperty.
 */
@Entity
@Table(name = "base_property")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BaseProperty implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 32)
    @Column(name = "property_id", length = 32)
    private String propertyID;

    @Size(max = 64)
    @Column(name = "property_name", length = 64)
    private String propertyName;

    @Size(max = 256)
    @Column(name = "property_describe", length = 256)
    private String propertyDescribe;

    @Size(max = 1)
    @Column(name = "property_type", length = 1)
    private String propertyType;

    @Column(name = "property_score")
    private Integer propertyScore;

    @Size(max = 1)
    @Column(name = "formy_type", length = 1)
    private String formyType;

    @Column(name = "completion_rate")
    private Integer completionRate;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPropertyID() {
        return propertyID;
    }

    public BaseProperty propertyID(String propertyID) {
        this.propertyID = propertyID;
        return this;
    }

    public void setPropertyID(String propertyID) {
        this.propertyID = propertyID;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public BaseProperty propertyName(String propertyName) {
        this.propertyName = propertyName;
        return this;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyDescribe() {
        return propertyDescribe;
    }

    public BaseProperty propertyDescribe(String propertyDescribe) {
        this.propertyDescribe = propertyDescribe;
        return this;
    }

    public void setPropertyDescribe(String propertyDescribe) {
        this.propertyDescribe = propertyDescribe;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public BaseProperty propertyType(String propertyType) {
        this.propertyType = propertyType;
        return this;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public Integer getPropertyScore() {
        return propertyScore;
    }

    public BaseProperty propertyScore(Integer propertyScore) {
        this.propertyScore = propertyScore;
        return this;
    }

    public void setPropertyScore(Integer propertyScore) {
        this.propertyScore = propertyScore;
    }

    public String getFormyType() {
        return formyType;
    }

    public BaseProperty formyType(String formyType) {
        this.formyType = formyType;
        return this;
    }

    public void setFormyType(String formyType) {
        this.formyType = formyType;
    }

    public Integer getCompletionRate() {
        return completionRate;
    }

    public BaseProperty completionRate(Integer completionRate) {
        this.completionRate = completionRate;
        return this;
    }

    public void setCompletionRate(Integer completionRate) {
        this.completionRate = completionRate;
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
        BaseProperty baseProperty = (BaseProperty) o;
        if (baseProperty.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), baseProperty.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BaseProperty{" +
            "id=" + getId() +
            ", propertyID='" + getPropertyID() + "'" +
            ", propertyName='" + getPropertyName() + "'" +
            ", propertyDescribe='" + getPropertyDescribe() + "'" +
            ", propertyType='" + getPropertyType() + "'" +
            ", propertyScore=" + getPropertyScore() +
            ", formyType='" + getFormyType() + "'" +
            ", completionRate=" + getCompletionRate() +
            "}";
    }
}
