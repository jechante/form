package com.schinta.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A WxInfo.
 */
@Entity
@Table(name = "wx_info")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class WxInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 32)
    @Column(name = "user_id", length = 32)
    private String userID;

    @Size(max = 32)
    @Column(name = "wx_open_id", length = 32)
    private String wxOpenID;

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

    public WxInfo userID(String userID) {
        this.userID = userID;
        return this;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getWxOpenID() {
        return wxOpenID;
    }

    public WxInfo wxOpenID(String wxOpenID) {
        this.wxOpenID = wxOpenID;
        return this;
    }

    public void setWxOpenID(String wxOpenID) {
        this.wxOpenID = wxOpenID;
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
        WxInfo wxInfo = (WxInfo) o;
        if (wxInfo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), wxInfo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WxInfo{" +
            "id=" + getId() +
            ", userID='" + getUserID() + "'" +
            ", wxOpenID='" + getWxOpenID() + "'" +
            "}";
    }
}
