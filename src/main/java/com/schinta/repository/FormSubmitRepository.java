package com.schinta.repository;

import com.schinta.domain.FormSubmit;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the FormSubmit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormSubmitRepository extends JpaRepository<FormSubmit, Long> {

}
