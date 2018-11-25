package com.schinta.repository;

import com.schinta.domain.BaseProperty;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the BaseProperty entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BasePropertyRepository extends JpaRepository<BaseProperty, Long> {

}
