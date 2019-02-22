package com.schinta.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.schinta.domain.enumeration.FieldType;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

@MappedSuperclass
public abstract class PropertyValue implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 属性值:男等
     */
    @Size(max = 1000)
    @ApiModelProperty(value = "属性值:男等")
    @Column(name = "property_value", length = 1000)
    private String propertyValue;

    /**
     * 记录更新情况
     */
    @Size(max = 2000)
    @ApiModelProperty(value = "记录更新情况")
    @Column(name = "remark", length = 2000)
    private String remark;

    @ManyToOne(fetch = FetchType.LAZY)    @JsonIgnoreProperties("properties")
    @NaturalId
    private WxUser wxUser;

    @ManyToOne(fetch = FetchType.LAZY)    @JsonIgnoreProperties("propertyValues")
    @NaturalId
    private BaseProperty base;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public PropertyValue propertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
        return this;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    public String getRemark() {
        return remark;
    }

    public PropertyValue remark(String remark) {
        this.remark = remark;
        return this;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public WxUser getWxUser() {
        return wxUser;
    }

    public PropertyValue wxUser(WxUser wxUser) {
        this.wxUser = wxUser;
        return this;
    }

    public void setWxUser(WxUser wxUser) {
        this.wxUser = wxUser;
    }

    public BaseProperty getBase() {
        return base;
    }

    public PropertyValue base(BaseProperty baseProperty) {
        this.base = baseProperty;
        return this;
    }

    public void setBase(BaseProperty baseProperty) {
        this.base = baseProperty;
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

        PropertyValue propertyValue = (PropertyValue) o;
        // 通过Id
//        if (propertyValue.getId() == null || getId() == null) {
//            return false;
//        }
//        return Objects.equals(getId(), propertyValue.getId());

//        // 通过NatureId
        if (propertyValue.getWxUser() == null || propertyValue.getBase() == null ||
            getWxUser() == null || getBase() == null) {
            return false;
        }
        return Objects.equals(wxUser.getId() + base.getId(), propertyValue.getWxUser().getId() + propertyValue.getBase().getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(wxUser.getId() + base.getId());
    }

    @Override
    public String toString() {
        return "PropertyValue{" +
            "id=" + getId() +
            ", propertyValue='" + getPropertyValue() + "'" +
            ", remark='" + getRemark() + "'" +
            "}";
    }

    @JsonIgnore
    public static Class getClassFromFieldType(FieldType fieldType) {
        if (fieldType.equals(FieldType.PROPERTY)) {
            return UserProperty.class;
        } else if (fieldType.equals(FieldType.DEMAND)) {
            return UserDemand.class;
        }
        return null;
    }

    @JsonIgnore
    public String getShownValue(ObjectMapper mapper) throws IOException {

        Object propertyObj = mapper.readValue(this.propertyValue, Object.class);
        String property = null;
        if (propertyObj instanceof String) { // 如果是字符串
            property = (String) propertyObj;
        } else if (propertyObj instanceof List) { // 如果是数组
            property = String.join("; ",(ArrayList) propertyObj);
        } else if (propertyObj instanceof Number) { // 如果是数字
            property = propertyObj.toString();
        } else if (propertyObj instanceof Map) { // 如果是map
            property = String.join("",((HashMap) propertyObj).values());
        }
        return  property;
    }
}
