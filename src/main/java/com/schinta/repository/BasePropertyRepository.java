package com.schinta.repository;

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

    List<BaseProperty> findAllByIdIn(Set<Long> ids);

    // 第一个查询有问题，查出来的baseProperty不完整
//    @Query("select distinct base_property from BaseProperty base_property left join fetch base_property.propertyValues property left join fetch base_property.demandValues demand " +
//        "where (property.wxUser.id = :id or property.id is null) and (demand.wxUser.id = :id or demand.id is null)")
    // 用on/with查询结果完整，但是无法配合fetch使用（感觉算个bug），只能不使用fetch
    @Query("select distinct base_property, property, demand from BaseProperty base_property left join base_property.propertyValues property on property.wxUser.id = :id " +
        "left join base_property.demandValues demand on demand.wxUser.id = :id")
    @QueryHints({ @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_PASS_DISTINCT_THROUGH, value ="false") })
    List<Object[]> findUserPropertyDemands(@Param("id") String id);

//    List<BaseProperty> findAllByB(Set<Long> ids);


}

