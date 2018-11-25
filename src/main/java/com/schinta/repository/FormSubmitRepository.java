package com.schinta.repository;

import com.schinta.domain.FormSubmit;
import com.schinta.domain.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the FormSubmit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormSubmitRepository extends JpaRepository<FormSubmit, Long> {
    @Query(value = "select * from FormSubmit formSubmit where formSubmit.dealflag =:dealflag")
    List<FormSubmit> findByDealFlag(@Param("dealflag") String dealflag);
}
