package com.schinta.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.schinta.domain.enumeration.MatchType;

import com.schinta.domain.enumeration.PushStatus;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;

/**
 * A UserMatch.
 * 可以考虑保存的时候userA和userB的id需要保证按照字符串排序，userA位于userB前面，这样存储的时候就无须双向判断
 */
@Entity
@Table(name = "user_match")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@NaturalIdCache
public class UserMatch implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 以下四项是效用阵相关字段
    /**
     * 以A的需求对B打分
     */
    @ApiModelProperty(value = "以A的需求对B打分")
    @Column(name = "score_ato_b")
    private Float scoreAtoB;

    @Column(name = "score_bto_a")
    private Float scoreBtoA;

    @Column(name = "score_total")
    private Float scoreTotal;

    /**
     * 匹配度，即匹配分除以可能的最高分
     */
    @ApiModelProperty(value = "匹配度，即匹配分除以可能的最高分")
    @Column(name = "ratio")
    private Float ratio;

    // 以下是与配对和推送相关的属性，仅在推送成功之后才更新
    /**
     * 如果是a对b的主动配对，b的排名数（位于1~pushLimit之间）
     */
    @ApiModelProperty(value = "如果是a对b的主动配对，b的排名数（位于1~pushLimit之间）")
    @Column(name = "rank_a")
    private Integer rankA;

    @Column(name = "rank_b")
    private Integer rankB;

    @Enumerated(EnumType.STRING)
    @Column(name = "match_type")
    private MatchType matchType;

    @Enumerated(EnumType.STRING)
    @Column(name = "push_status")
    private PushStatus pushStatus;

    /**
     * 用户配对绩效结果-结果推送
     */
    @ApiModelProperty(value = "用户配对绩效结果-结果推送")
    @ManyToMany    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "user_match_push_records",
               joinColumns = @JoinColumn(name = "user_matches_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "push_records_id", referencedColumnName = "id"))
    private Set<PushRecord> pushRecords = new HashSet<>();

    /**
     * 算法
     */
    @ApiModelProperty(value = "算法")
    @ManyToOne(fetch = FetchType.LAZY)    @JsonIgnoreProperties("matches")
    @NaturalId
    private Algorithm algorithm;

    @ManyToOne(fetch = FetchType.LAZY)    @JsonIgnoreProperties("aMatches")
    @NaturalId
    private WxUser userA;

    @ManyToOne(fetch = FetchType.LAZY)    @JsonIgnoreProperties("bMatches")
    @NaturalId
    private WxUser userB;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getScoreAtoB() {
        return scoreAtoB;
    }

    public UserMatch scoreAtoB(Float scoreAtoB) {
        this.scoreAtoB = scoreAtoB;
        return this;
    }

    public void setScoreAtoB(Float scoreAtoB) {
        this.scoreAtoB = scoreAtoB;
    }

    public Float getScoreBtoA() {
        return scoreBtoA;
    }

    public UserMatch scoreBtoA(Float scoreBtoA) {
        this.scoreBtoA = scoreBtoA;
        return this;
    }

    public void setScoreBtoA(Float scoreBtoA) {
        this.scoreBtoA = scoreBtoA;
    }

    public Float getScoreTotal() {
        return scoreTotal;
    }

    public UserMatch scoreTotal(Float scoreTotal) {
        this.scoreTotal = scoreTotal;
        return this;
    }

    public void setScoreTotal(Float scoreTotal) {
        this.scoreTotal = scoreTotal;
    }

    public Float getRatio() {
        return ratio;
    }

    public UserMatch ratio(Float ratio) {
        this.ratio = ratio;
        return this;
    }

    public void setRatio(Float ratio) {
        this.ratio = ratio;
    }

    public Integer getRankA() {
        return rankA;
    }

    public UserMatch rankA(Integer rankA) {
        this.rankA = rankA;
        return this;
    }

    public void setRankA(Integer rankA) {
        this.rankA = rankA;
    }

    public Integer getRankB() {
        return rankB;
    }

    public UserMatch rankB(Integer rankB) {
        this.rankB = rankB;
        return this;
    }

    public void setRankB(Integer rankB) {
        this.rankB = rankB;
    }

    public MatchType getMatchType() {
        return matchType;
    }

    public UserMatch matchType(MatchType matchType) {
        this.matchType = matchType;
        return this;
    }

    public void setMatchType(MatchType matchType) {
        this.matchType = matchType;
    }

    public PushStatus getPushStatus() {
        return pushStatus;
    }

    public UserMatch pushStatus(PushStatus pushStatus) {
        this.pushStatus = pushStatus;
        return this;
    }

    public void setPushStatus(PushStatus pushStatus) {
        this.pushStatus = pushStatus;
    }

    public Set<PushRecord> getPushRecords() {
        return pushRecords;
    }

    public UserMatch pushRecords(Set<PushRecord> pushRecords) {
        this.pushRecords = pushRecords;
        return this;
    }

    public UserMatch addPushRecords(PushRecord pushRecord) {
        this.pushRecords.add(pushRecord);
        pushRecord.getUserMatches().add(this);
        return this;
    }

    public UserMatch removePushRecords(PushRecord pushRecord) {
        this.pushRecords.remove(pushRecord);
        pushRecord.getUserMatches().remove(this);
        return this;
    }

    public void setPushRecords(Set<PushRecord> pushRecords) {
        this.pushRecords = pushRecords;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public UserMatch algorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
        return this;
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public WxUser getUserA() {
        return userA;
    }

    public UserMatch userA(WxUser wxUser) {
        this.userA = wxUser;
        return this;
    }

    public void setUserA(WxUser wxUser) {
        this.userA = wxUser;
    }

    public WxUser getUserB() {
        return userB;
    }

    public UserMatch userB(WxUser wxUser) {
        this.userB = wxUser;
        return this;
    }

    public void setUserB(WxUser wxUser) {
        this.userB = wxUser;
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
        UserMatch userMatch = (UserMatch) o;
//        if (userMatch.getId() == null || getId() == null) {
//            return false;
//        }
//        return Objects.equals(getId(), userMatch.getId());

        if (userMatch.getUserA() == null || userMatch.getUserB() == null || userMatch.getAlgorithm() == null ||
        getUserA() == null || getUserB() == null || getAlgorithm() == null) {
            return false;
        }
        return Objects.equals(userA.getId()+userB.getId()+algorithm.getId(), userMatch.getUserA().getId()+userMatch.getUserB().getId()+userMatch.getAlgorithm().getId());
    }

    @Override
    public int hashCode() {
//        return Objects.hashCode(getId());
        return Objects.hashCode(userA.getId()+userB.getId()+algorithm.getId());
    }

    @Override
    public String toString() {
        return "UserMatch{" +
            "id=" + getId() +
            ", scoreAtoB=" + getScoreAtoB() +
            ", scoreBtoA=" + getScoreBtoA() +
            ", scoreTotal=" + getScoreTotal() +
            ", ratio=" + getRatio() +
            ", rankA=" + getRankA() +
            ", rankB=" + getRankB() +
            ", matchType='" + getMatchType() + "'" +
            ", pushStatus='" + getPushStatus() + "'" +
            "}";
    }

    // 是否已经推送给A
    @JsonIgnore
    public boolean hasPushedToA() {
        return pushStatus != null && pushStatus.equals(PushStatus.A);
    }

    // 是否已经推送给B
    @JsonIgnore
    public boolean hasPushedToB() {
        return pushStatus != null && pushStatus.equals(PushStatus.B);
    }

    // 是否已经推送给任意一方
    @JsonIgnore
    public boolean hasPushedToEither() {
        return pushStatus != null && (pushStatus.equals(PushStatus.A) || pushStatus.equals(PushStatus.B));
    }

    // 是否可以被推送给指定用户
    @JsonIgnore
    public boolean toBePushedToUser(WxUser user) {
        if (userA.equals(user) && (!hasPushedToA())) {
            return true;
        } else if (userB.equals(user) && (!hasPushedToB())) {
            return true;
        } else return false;
    }




}
