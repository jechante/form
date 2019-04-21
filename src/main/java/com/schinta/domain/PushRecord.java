package com.schinta.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.schinta.domain.enumeration.PushType;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A PushRecord.
 */
@Entity
@Table(name = "push_record")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PushRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "push_type")
    private PushType pushType;

    /**
     * 推送时间
     */
    @ApiModelProperty(value = "推送时间")
    @Column(name = "push_date_time")
    private LocalDateTime pushDateTime;

    /**
     * 是否推送成功
     */
    @ApiModelProperty(value = "是否推送成功")
    @Column(name = "success")
    private Boolean success;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("pushRecords")
    private WxUser user;

    /**
     * 群发结果地址
     */
    @Size(max = 2000)
    @ApiModelProperty(value = "群发结果地址")
    @Column(name = "result_url", length = 2000)
    private String resultUrl;


    /**
     * 用户配对绩效结果-结果推送
     */
    @ApiModelProperty(value = "用户配对绩效结果-结果推送")
    @ManyToMany    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "user_match_push_records",
        joinColumns = @JoinColumn(name = "push_records_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "user_matches_id", referencedColumnName = "id"))
    private Set<UserMatch> userMatches = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove


    public String getResultUrl() {
        return resultUrl;
    }

    public void setResultUrl(String resultUrl) {
        this.resultUrl = resultUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PushType getPushType() {
        return pushType;
    }

    public PushRecord pushType(PushType pushType) {
        this.pushType = pushType;
        return this;
    }

    public void setPushType(PushType pushType) {
        this.pushType = pushType;
    }

    public LocalDateTime getPushDateTime() {
        return pushDateTime;
    }

    public PushRecord pushDateTime(LocalDateTime pushDateTime) {
        this.pushDateTime = pushDateTime;
        return this;
    }

    public void setPushDateTime(LocalDateTime pushDateTime) {
        this.pushDateTime = pushDateTime;
    }

    public Boolean isSuccess() {
        return success;
    }

    public PushRecord success(Boolean success) {
        this.success = success;
        return this;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public WxUser getUser() {
        return user;
    }

    public PushRecord user(WxUser wxUser) {
        this.user = wxUser;
        return this;
    }

    public void setUser(WxUser wxUser) {
        this.user = wxUser;
    }

    public Set<UserMatch> getUserMatches() {
        return userMatches;
    }

    public PushRecord userMatches(Set<UserMatch> userMatches) {
        this.userMatches = userMatches;
        return this;
    }

    public PushRecord addUserMatches(UserMatch userMatch) {
        this.userMatches.add(userMatch);
        userMatch.getPushRecords().add(this);
        return this;
    }

    public PushRecord removeUserMatches(UserMatch userMatch) {
        this.userMatches.remove(userMatch);
        userMatch.getPushRecords().remove(this);
        return this;
    }

    public void setUserMatches(Set<UserMatch> userMatches) {
        this.userMatches = userMatches;
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
        PushRecord pushRecord = (PushRecord) o;
        if (pushRecord.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pushRecord.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PushRecord{" +
            "id=" + getId() +
            ", pushType='" + getPushType() + "'" +
            ", pushDateTime='" + getPushDateTime() + "'" +
            ", success='" + isSuccess() + "'" +
            "}";
    }
}
