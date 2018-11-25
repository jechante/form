package com.schinta.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A MatchRecord.
 */
@Entity
@Table(name = "match_record")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MatchRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 32)
    @Column(name = "record_id", length = 32)
    private String recordID;

    @Size(max = 32)
    @Column(name = "user_a", length = 32)
    private String userA;

    @Size(max = 32)
    @Column(name = "user_b", length = 32)
    private String userB;

    @Size(max = 32)
    @Column(name = "algorithm_id", length = 32)
    private String algorithmID;

    @Size(max = 1)
    @Column(name = "initiator_type", length = 1)
    private String initiatorType;

    @Column(name = "score_ato_b")
    private Integer scoreAtoB;

    @Column(name = "score_bto_a")
    private Integer scoreBtoA;

    @Column(name = "score_total")
    private Integer scoreTotal;

    @Column(name = "gender_weight")
    private Double genderWeight;

    @Size(max = 2000)
    @Column(name = "property_a", length = 2000)
    private String propertyA;

    @Size(max = 2000)
    @Column(name = "property_b", length = 2000)
    private String propertyB;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRecordID() {
        return recordID;
    }

    public MatchRecord recordID(String recordID) {
        this.recordID = recordID;
        return this;
    }

    public void setRecordID(String recordID) {
        this.recordID = recordID;
    }

    public String getUserA() {
        return userA;
    }

    public MatchRecord userA(String userA) {
        this.userA = userA;
        return this;
    }

    public void setUserA(String userA) {
        this.userA = userA;
    }

    public String getUserB() {
        return userB;
    }

    public MatchRecord userB(String userB) {
        this.userB = userB;
        return this;
    }

    public void setUserB(String userB) {
        this.userB = userB;
    }

    public String getAlgorithmID() {
        return algorithmID;
    }

    public MatchRecord algorithmID(String algorithmID) {
        this.algorithmID = algorithmID;
        return this;
    }

    public void setAlgorithmID(String algorithmID) {
        this.algorithmID = algorithmID;
    }

    public String getInitiatorType() {
        return initiatorType;
    }

    public MatchRecord initiatorType(String initiatorType) {
        this.initiatorType = initiatorType;
        return this;
    }

    public void setInitiatorType(String initiatorType) {
        this.initiatorType = initiatorType;
    }

    public Integer getScoreAtoB() {
        return scoreAtoB;
    }

    public MatchRecord scoreAtoB(Integer scoreAtoB) {
        this.scoreAtoB = scoreAtoB;
        return this;
    }

    public void setScoreAtoB(Integer scoreAtoB) {
        this.scoreAtoB = scoreAtoB;
    }

    public Integer getScoreBtoA() {
        return scoreBtoA;
    }

    public MatchRecord scoreBtoA(Integer scoreBtoA) {
        this.scoreBtoA = scoreBtoA;
        return this;
    }

    public void setScoreBtoA(Integer scoreBtoA) {
        this.scoreBtoA = scoreBtoA;
    }

    public Integer getScoreTotal() {
        return scoreTotal;
    }

    public MatchRecord scoreTotal(Integer scoreTotal) {
        this.scoreTotal = scoreTotal;
        return this;
    }

    public void setScoreTotal(Integer scoreTotal) {
        this.scoreTotal = scoreTotal;
    }

    public Double getGenderWeight() {
        return genderWeight;
    }

    public MatchRecord genderWeight(Double genderWeight) {
        this.genderWeight = genderWeight;
        return this;
    }

    public void setGenderWeight(Double genderWeight) {
        this.genderWeight = genderWeight;
    }

    public String getPropertyA() {
        return propertyA;
    }

    public MatchRecord propertyA(String propertyA) {
        this.propertyA = propertyA;
        return this;
    }

    public void setPropertyA(String propertyA) {
        this.propertyA = propertyA;
    }

    public String getPropertyB() {
        return propertyB;
    }

    public MatchRecord propertyB(String propertyB) {
        this.propertyB = propertyB;
        return this;
    }

    public void setPropertyB(String propertyB) {
        this.propertyB = propertyB;
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
        MatchRecord matchRecord = (MatchRecord) o;
        if (matchRecord.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), matchRecord.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MatchRecord{" +
            "id=" + getId() +
            ", recordID='" + getRecordID() + "'" +
            ", userA='" + getUserA() + "'" +
            ", userB='" + getUserB() + "'" +
            ", algorithmID='" + getAlgorithmID() + "'" +
            ", initiatorType='" + getInitiatorType() + "'" +
            ", scoreAtoB=" + getScoreAtoB() +
            ", scoreBtoA=" + getScoreBtoA() +
            ", scoreTotal=" + getScoreTotal() +
            ", genderWeight=" + getGenderWeight() +
            ", propertyA='" + getPropertyA() + "'" +
            ", propertyB='" + getPropertyB() + "'" +
            "}";
    }
}
