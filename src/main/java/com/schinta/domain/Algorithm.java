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

    /**
     * 算法名
     */
    @NotNull
    @Size(max = 64)
    @ApiModelProperty(value = "算法名", required = true)
    @Column(name = "name", length = 64, nullable = false)
    private String name;

    /**
     * 过滤属性
     */
    @Size(max = 2000)
    @ApiModelProperty(value = "过滤属性")
    @Column(name = "filter_strings", length = 2000)
    private String filterStrings;

    /**
     * 计分属性
     */
    @Size(max = 2000)
    @ApiModelProperty(value = "计分属性")
    @Column(name = "score_strings", length = 2000)
    private String scoreStrings;

    /**
     * 需求权重
     */
    @ApiModelProperty(value = "需求权重")
    @Column(name = "gender_weight")
    private Double genderWeight;

    /**
     * 分块数
     */
    @ApiModelProperty(value = "分块数")
    @Column(name = "k_value")
    private Integer kValue;

    /**
     * 算法说明
     */
    @Size(max = 2000)
    @ApiModelProperty(value = "算法说明")
    @Column(name = "remark", length = 2000)
    private String remark;

    /**
     * 是否启用
     */
    @ApiModelProperty(value = "是否启用")
    @Column(name = "enabled")
    private Boolean enabled;

    /**
     * 用户配对绩效结果
     */
    @ApiModelProperty(value = "用户配对绩效结果")
    @OneToMany(mappedBy = "algorithm")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<UserMatch> matches = new HashSet<>();
    /**
     * 过滤属性
     */
    @ApiModelProperty(value = "过滤属性")
    @ManyToMany    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "algorithm_filter_properties",
               joinColumns = @JoinColumn(name = "algorithms_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "filter_properties_id", referencedColumnName = "id"))
    private Set<BaseProperty> filterProperties = new HashSet<>();

    /**
     * 计分属性
     */
    @ApiModelProperty(value = "计分属性")
    @ManyToMany    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "algorithm_score_properties",
               joinColumns = @JoinColumn(name = "algorithms_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "score_properties_id", referencedColumnName = "id"))
    private Set<BaseProperty> scoreProperties = new HashSet<>();

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

    public Algorithm name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
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

    public Integer getkValue() {
        return kValue;
    }

    public Algorithm kValue(Integer kValue) {
        this.kValue = kValue;
        return this;
    }

    public void setkValue(Integer kValue) {
        this.kValue = kValue;
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

    public Boolean isEnabled() {
        return enabled;
    }

    public Algorithm enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Set<UserMatch> getMatches() {
        return matches;
    }

    public Algorithm matches(Set<UserMatch> userMatches) {
        this.matches = userMatches;
        return this;
    }

    public Algorithm addMatches(UserMatch userMatch) {
        this.matches.add(userMatch);
        userMatch.setAlgorithm(this);
        return this;
    }

    public Algorithm removeMatches(UserMatch userMatch) {
        this.matches.remove(userMatch);
        userMatch.setAlgorithm(null);
        return this;
    }

    public void setMatches(Set<UserMatch> userMatches) {
        this.matches = userMatches;
    }

    public Set<BaseProperty> getFilterProperties() {
        return filterProperties;
    }

    public Algorithm filterProperties(Set<BaseProperty> baseProperties) {
        this.filterProperties = baseProperties;
        return this;
    }

    public Algorithm addFilterProperties(BaseProperty baseProperty) {
        this.filterProperties.add(baseProperty);
        baseProperty.getFilterAlgorithms().add(this);
        return this;
    }

    public Algorithm removeFilterProperties(BaseProperty baseProperty) {
        this.filterProperties.remove(baseProperty);
        baseProperty.getFilterAlgorithms().remove(this);
        return this;
    }

    public void setFilterProperties(Set<BaseProperty> baseProperties) {
        this.filterProperties = baseProperties;
    }

    public Set<BaseProperty> getScoreProperties() {
        return scoreProperties;
    }

    public Algorithm scoreProperties(Set<BaseProperty> baseProperties) {
        this.scoreProperties = baseProperties;
        return this;
    }

    public Algorithm addScoreProperties(BaseProperty baseProperty) {
        this.scoreProperties.add(baseProperty);
        baseProperty.getScoreAlgorithms().add(this);
        return this;
    }

    public Algorithm removeScoreProperties(BaseProperty baseProperty) {
        this.scoreProperties.remove(baseProperty);
        baseProperty.getScoreAlgorithms().remove(this);
        return this;
    }

    public void setScoreProperties(Set<BaseProperty> baseProperties) {
        this.scoreProperties = baseProperties;
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
            ", name='" + getName() + "'" +
            ", filterStrings='" + getFilterStrings() + "'" +
            ", scoreStrings='" + getScoreStrings() + "'" +
            ", genderWeight=" + getGenderWeight() +
            ", kValue=" + getkValue() +
            ", remark='" + getRemark() + "'" +
            ", enabled='" + isEnabled() + "'" +
            "}";
    }
}
