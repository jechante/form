package com.schinta.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A UserProperty.
 */
@Entity
@Table(name = "user_property")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserProperty implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 32)
    @Column(name = "user_id", length = 32, nullable = false)
    private String userID;

    @Size(max = 32)
    @Column(name = "property_id", length = 32)
    private String propertyID;

    @Size(max = 64)
    @Column(name = "property_value", length = 64)
    private String propertyValue;

    @Size(max = 2000)
    @Column(name = "remark", length = 2000)
    private String remark;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public UserProperty userID(String userID) {
        this.userID = userID;
        return this;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPropertyID() {
        return propertyID;
    }

    public UserProperty propertyID(String propertyID) {
        this.propertyID = propertyID;
        return this;
    }

    public void setPropertyID(String propertyID) {
        this.propertyID = propertyID;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public UserProperty propertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
        return this;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    public String getRemark() {
        return remark;
    }

    public UserProperty remark(String remark) {
        this.remark = remark;
        return this;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
        UserProperty userProperty = (UserProperty) o;
        if (userProperty.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userProperty.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserProperty{" +
            "id=" + getId() +
            ", userID='" + getUserID() + "'" +
            ", propertyID='" + getPropertyID() + "'" +
            ", propertyValue='" + getPropertyValue() + "'" +
            ", remark='" + getRemark() + "'" +
            "}";
    }
}
