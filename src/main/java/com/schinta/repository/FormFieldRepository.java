package com.schinta.repository;

import com.schinta.domain.BaseForm;
import com.schinta.domain.FormField;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the FormField entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormFieldRepository extends JpaRepository<FormField, Long> {

    List<FormField> findAllByBaseForm(BaseForm baseForm);
}
