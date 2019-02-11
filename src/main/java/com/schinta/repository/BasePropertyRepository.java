package com.schinta.repository;

import com.schinta.domain.BaseProperty;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;


/**
 * Spring Data  repository for the BaseProperty entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BasePropertyRepository extends JpaRepository<BaseProperty, Long> {

    List<BaseProperty> findAllByIdIn(Set<Long> ids);

//    List<BaseProperty> findAllByB(Set<Long> ids);


}

