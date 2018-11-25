package com.schinta.repository;

import com.schinta.domain.BaseForm;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the BaseForm entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BaseFormRepository extends JpaRepository<BaseForm, Long> {

}
