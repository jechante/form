package com.schinta.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Algorithm.
 */
@Entity
@Table(name = "algorithm")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Algorithm implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 32)
    @Column(name = "algorithm_id", length = 32)
    private String algorithmID;

    @Size(max = 2000)
    @Column(name = "filter_strings", length = 2000)
    private String filterStrings;

    @Size(max = 2000)
    @Column(name = "score_strings", length = 2000)
    private String scoreStrings;

    @Column(name = "gender_weight")
    private Double genderWeight;

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

    public String getAlgorithmID() {
        return algorithmID;
    }

    public Algorithm algorithmID(String algorithmID) {
        this.algorithmID = algorithmID;
        return this;
    }

    public void setAlgorithmID(String algorithmID) {
        this.algorithmID = algorithmID;
    }

    public String getFilterStrings() {
        return filterStrings;
    }

    public Algorithm filterStrings(String filterStrings) {
        this.filterStrings = filterStrings;
        return this;
    }

    public void setFilterStrings(String filterStrings) {
        this.filterStrings = filterStrings;
    }

    public String getScoreStrings() {
        return scoreStrings;
    }

    public Algorithm scoreStrings(String scoreStrings) {
        this.scoreStrings = scoreStrings;
        return this;
    }

    public void setScoreStrings(String scoreStrings) {
        this.scoreStrings = scoreStrings;
    }

    public Double getGenderWeight() {
        return genderWeight;
    }

    public Algorithm genderWeight(Double genderWeight) {
        this.genderWeight = genderWeight;
        return this;
    }

    public void setGenderWeight(Double genderWeight) {
        this.genderWeight = genderWeight;
    }

    public String getRemark() {
        return remark;
    }

    public Algorithm remark(String remark) {
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
        Algorithm algorithm = (Algorithm) o;
        if (algorithm.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), algorithm.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Algorithm{" +
            "id=" + getId() +
            ", algorithmID='" + getAlgorithmID() + "'" +
            ", filterStrings='" + getFilterStrings() + "'" +
            ", scoreStrings='" + getScoreStrings() + "'" +
            ", genderWeight=" + getGenderWeight() +
            ", remark='" + getRemark() + "'" +
            "}";
    }
}
