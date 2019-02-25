package com.schinta.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.schinta.domain.enumeration.PropertyType;

import com.schinta.domain.enumeration.FormyType;

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

    /**
     * 属性名称：身高等
     */
    @NotNull
    @Size(max = 64)
    @ApiModelProperty(value = "属性名称：身高等", required = true)
    @Column(name = "property_name", length = 64, nullable = false)
    private String propertyName;

    /**
     * 属性描述
     */
    @Size(max = 256)
    @ApiModelProperty(value = "属性描述")
    @Column(name = "property_describe", length = 256)
    private String propertyDescribe;

    /**
     * 属性类型：三观、性格、外貌等
     */
    @ApiModelProperty(value = "属性类型：三观、性格、外貌等")
    @Enumerated(EnumType.STRING)
    @Column(name = "property_type")
    private PropertyType propertyType;

    /**
     * 属性分值
     */
    @ApiModelProperty(value = "属性分值")
    @Column(name = "property_score")
    private Integer propertyScore;

    /**
     * 属性最高分
     */
    @ApiModelProperty(value = "属性最高分")
    @Column(name = "property_max_score")
    private Integer propertyMaxScore;

    /**
     * 匹配类型：1对1、1对多、多对多、其他(不计分项，例如照片等)
     */
    @ApiModelProperty(value = "匹配类型：1对1、1对多、多对多、其他(不计分项，例如照片等)")
    @Enumerated(EnumType.STRING)
    @Column(name = "formy_type")
    private FormyType formyType;

    /**
     * 对完成率所占比重：5等
     */
    @ApiModelProperty(value = "对完成率所占比重：5等")
    @Column(name = "completion_rate")
    private Integer completionRate;

    /**
     * 属性-用户属性值
     */
    @ApiModelProperty(value = "属性-用户属性值")
    @OneToMany(mappedBy = "base")
    @JsonIgnoreProperties("base")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<UserProperty> propertyValues = new HashSet<>();
    /**
     * 属性-用户需求值
     */
    @ApiModelProperty(value = "属性-用户需求值")
    @OneToMany(mappedBy = "base")
    @JsonIgnoreProperties("base")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<UserDemand> demandValues = new HashSet<>();
    /**
     * 属性-表单字段
     */
    @ApiModelProperty(value = "属性-表单字段")
    @OneToMany(mappedBy = "baseProperty")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<FormField> fields = new HashSet<>();
    /**
     * 作为过滤属性参与的算法
     */
    @ApiModelProperty(value = "作为过滤属性参与的算法")
    @ManyToMany(mappedBy = "filterProperties")    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonIgnore    private Set<Algorithm> filterAlgorithms = new HashSet<>();

    /**
     * 作为计分属性参与的算法
     */
    @ApiModelProperty(value = "作为计分属性参与的算法")
    @ManyToMany(mappedBy = "scoreProperties")    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonIgnore    private Set<Algorithm> scoreAlgorithms = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public BaseProperty propertyType(PropertyType propertyType) {
        this.propertyType = propertyType;
        return this;
    }

    public void setPropertyType(PropertyType propertyType) {
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

    public Integer getPropertyMaxScore() {
        return propertyMaxScore;
    }

    public BaseProperty propertyMaxScore(Integer propertyMaxScore) {
        this.propertyMaxScore = propertyMaxScore;
        return this;
    }

    public void setPropertyMaxScore(Integer propertyMaxScore) {
        this.propertyMaxScore = propertyMaxScore;
    }

    public FormyType getFormyType() {
        return formyType;
    }

    public BaseProperty formyType(FormyType formyType) {
        this.formyType = formyType;
        return this;
    }

    public void setFormyType(FormyType formyType) {
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

    public Set<UserProperty> getPropertyValues() {
        return propertyValues;
    }

    public BaseProperty propertyValues(Set<UserProperty> userProperties) {
        this.propertyValues = userProperties;
        return this;
    }

    public BaseProperty addPropertyValues(UserProperty userProperty) {
        if (userProperty != null ) {
            this.propertyValues.add(userProperty);
            userProperty.setBase(this);
        }
        return this;
    }

    public BaseProperty removePropertyValues(UserProperty userProperty) {
        this.propertyValues.remove(userProperty);
        userProperty.setBase(null);
        return this;
    }

    public void setPropertyValues(Set<UserProperty> userProperties) {
        this.propertyValues = userProperties;
    }

    public Set<UserDemand> getDemandValues() {
        return demandValues;
    }

    public BaseProperty demandValues(Set<UserDemand> userDemands) {
        this.demandValues = userDemands;
        return this;
    }

    public BaseProperty addDemandValues(UserDemand userDemand) {
        if (userDemand != null) {
            this.demandValues.add(userDemand);
            userDemand.setBase(this);
        }
        return this;
    }

    public BaseProperty removeDemandValues(UserDemand userDemand) {
        this.demandValues.remove(userDemand);
        userDemand.setBase(null);
        return this;
    }

    public void setDemandValues(Set<UserDemand> userDemands) {
        this.demandValues = userDemands;
    }

    public Set<FormField> getFields() {
        return fields;
    }

    public BaseProperty fields(Set<FormField> formFields) {
        this.fields = formFields;
        return this;
    }

    public BaseProperty addFields(FormField formField) {
        this.fields.add(formField);
        formField.setBaseProperty(this);
        return this;
    }

    public BaseProperty removeFields(FormField formField) {
        this.fields.remove(formField);
        formField.setBaseProperty(null);
        return this;
    }

    public void setFields(Set<FormField> formFields) {
        this.fields = formFields;
    }

    public Set<Algorithm> getFilterAlgorithms() {
        return filterAlgorithms;
    }

    public BaseProperty filterAlgorithms(Set<Algorithm> algorithms) {
        this.filterAlgorithms = algorithms;
        return this;
    }

    public BaseProperty addFilterAlgorithms(Algorithm algorithm) {
        this.filterAlgorithms.add(algorithm);
        algorithm.getFilterProperties().add(this);
        return this;
    }

    public BaseProperty removeFilterAlgorithms(Algorithm algorithm) {
        this.filterAlgorithms.remove(algorithm);
        algorithm.getFilterProperties().remove(this);
        return this;
    }

    public void setFilterAlgorithms(Set<Algorithm> algorithms) {
        this.filterAlgorithms = algorithms;
    }

    public Set<Algorithm> getScoreAlgorithms() {
        return scoreAlgorithms;
    }

    public BaseProperty scoreAlgorithms(Set<Algorithm> algorithms) {
        this.scoreAlgorithms = algorithms;
        return this;
    }

    public BaseProperty addScoreAlgorithms(Algorithm algorithm) {
        this.scoreAlgorithms.add(algorithm);
        algorithm.getScoreProperties().add(this);
        return this;
    }

    public BaseProperty removeScoreAlgorithms(Algorithm algorithm) {
        this.scoreAlgorithms.remove(algorithm);
        algorithm.getScoreProperties().remove(this);
        return this;
    }

    public void setScoreAlgorithms(Set<Algorithm> algorithms) {
        this.scoreAlgorithms = algorithms;
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
            ", propertyName='" + getPropertyName() + "'" +
            ", propertyDescribe='" + getPropertyDescribe() + "'" +
            ", propertyType='" + getPropertyType() + "'" +
            ", propertyScore=" + getPropertyScore() +
            ", propertyMaxScore=" + getPropertyMaxScore() +
            ", formyType='" + getFormyType() + "'" +
            ", completionRate=" + getCompletionRate() +
            "}";
    }
}
