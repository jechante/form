package com.schinta.repository;

import com.schinta.domain.BaseForm;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the BaseForm entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BaseFormRepository extends JpaRepository<BaseForm, Long> {

//    @Override
    Optional<BaseForm> findByEnabled(Boolean enabled);

    Optional<BaseForm> findByFormCode(String formCode);
}
