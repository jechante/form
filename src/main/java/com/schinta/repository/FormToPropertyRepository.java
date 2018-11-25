package com.schinta.repository;

import com.schinta.domain.FormToProperty;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the FormToProperty entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormToPropertyRepository extends JpaRepository<FormToProperty, Long> {

}
