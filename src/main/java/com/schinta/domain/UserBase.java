package com.schinta.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A UserBase.
 */
@Entity
@Table(name = "user_base")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 32)
    @Column(name = "user_id", length = 32, nullable = false)
    private String userID;

    @Size(max = 64)
    @Column(name = "user_name", length = 64)
    private String userName;

    @Size(max = 1)
    @Column(name = "user_status", length = 1)
    private String userStatus;

    @Size(max = 1)
    @Column(name = "sex", length = 1)
    private String sex;

    @Size(max = 1)
    @Column(name = "register_channel", length = 1)
    private String registerChannel;

    @Column(name = "register_broker")
    private Integer registerBroker;

    @Column(name = "register_date")
    private Integer registerDate;

    @Column(name = "register_time")
    private Integer registerTime;

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

    public UserBase userID(String userID) {
        this.userID = userID;
        return this;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public UserBase userName(String userName) {
        this.userName = userName;
        return this;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public UserBase userStatus(String userStatus) {
        this.userStatus = userStatus;
        return this;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getSex() {
        return sex;
    }

    public UserBase sex(String sex) {
        this.sex = sex;
        return this;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getRegisterChannel() {
        return registerChannel;
    }

    public UserBase registerChannel(String registerChannel) {
        this.registerChannel = registerChannel;
        return this;
    }

    public void setRegisterChannel(String registerChannel) {
        this.registerChannel = registerChannel;
    }

    public Integer getRegisterBroker() {
        return registerBroker;
    }

    public UserBase registerBroker(Integer registerBroker) {
        this.registerBroker = registerBroker;
        return this;
    }

    public void setRegisterBroker(Integer registerBroker) {
        this.registerBroker = registerBroker;
    }

    public Integer getRegisterDate() {
        return registerDate;
    }

    public UserBase registerDate(Integer registerDate) {
        this.registerDate = registerDate;
        return this;
    }

    public void setRegisterDate(Integer registerDate) {
        this.registerDate = registerDate;
    }

    public Integer getRegisterTime() {
        return registerTime;
    }

    public UserBase registerTime(Integer registerTime) {
        this.registerTime = registerTime;
        return this;
    }

    public void setRegisterTime(Integer registerTime) {
        this.registerTime = registerTime;
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
        UserBase userBase = (UserBase) o;
        if (userBase.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userBase.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserBase{" +
            "id=" + getId() +
            ", userID='" + getUserID() + "'" +
            ", userName='" + getUserName() + "'" +
            ", userStatus='" + getUserStatus() + "'" +
            ", sex='" + getSex() + "'" +
            ", registerChannel='" + getRegisterChannel() + "'" +
            ", registerBroker=" + getRegisterBroker() +
            ", registerDate=" + getRegisterDate() +
            ", registerTime=" + getRegisterTime() +
            "}";
    }
}
