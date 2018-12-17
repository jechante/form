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

/**
 * A Broker.
 */
@Entity
@Table(name = "broker")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Broker implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 广告经济商名称
     */
    @Size(max = 32)
    @ApiModelProperty(value = "广告经济商名称")
    @Column(name = "name", length = 32)
    private String name;

    /**
     * 广告经济商-微信用户
     */
    @ApiModelProperty(value = "广告经济商-微信用户")
    @OneToMany(mappedBy = "broker")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<WxUser> wxUsers = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Broker name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<WxUser> getWxUsers() {
        return wxUsers;
    }

    public Broker wxUsers(Set<WxUser> wxUsers) {
        this.wxUsers = wxUsers;
        return this;
    }

    public Broker addWxUsers(WxUser wxUser) {
        this.wxUsers.add(wxUser);
        wxUser.setBroker(this);
        return this;
    }

    public Broker removeWxUsers(WxUser wxUser) {
        this.wxUsers.remove(wxUser);
        wxUser.setBroker(null);
        return this;
    }

    public void setWxUsers(Set<WxUser> wxUsers) {
        this.wxUsers = wxUsers;
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
        Broker broker = (Broker) o;
        if (broker.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), broker.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Broker{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
