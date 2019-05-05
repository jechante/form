package com.schinta.repository;

import com.schinta.domain.BaseForm;
import com.schinta.domain.BaseProperty;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.List;
import java.util.Set;


/**
 * Spring Data  repository for the BaseProperty entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BasePropertyRepository extends JpaRepository<BaseProperty, Long> {

    @Query("select base_property from BaseProperty base_property where base_property.id in :ids order by base_property.serialNumber nulls last ")
    List<BaseProperty> findAllByIdIn(@Param("ids") Set<Long> ids);

    // 第一个查询有问题，查出来的baseProperty不完整
//    @Query("select distinct base_property from BaseProperty base_property left join fetch base_property.propertyValues property left join fetch base_property.demandValues demand " +
//        "where (property.wxUser.id = :id or property.id is null) and (demand.wxUser.id = :id or demand.id is null)")
    // 用on/with查询结果完整，但是无法配合fetch使用（感觉算个bug），只能不使用fetch
    @Query("select distinct base_property, property, demand from BaseProperty base_property left join base_property.propertyValues property on property.wxUser.id = :id " +
        "left join base_property.demandValues demand on demand.wxUser.id = :id")
    @QueryHints({ @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_PASS_DISTINCT_THROUGH, value ="false") })
    List<Object[]> findUserPropertyDemands(@Param("id") String id);

//    List<BaseProperty> findAllByB(Set<Long> ids);
    @Query("select base_property from BaseProperty base_property where base_property.propertyName = '性别'")
    BaseProperty findSex();

    // 查询某个表单所涵盖的所有属性
    @Query("select base_property from BaseProperty base_property join base_property.fields form_field where form_field.baseForm = :baseForm")
    List<BaseProperty> findBaseFormProperty(@Param("baseForm") BaseForm baseForm);

}

