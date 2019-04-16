package com.schinta.repository;

import com.schinta.domain.BaseForm;
import com.schinta.domain.FormField;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the FormField entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormFieldRepository extends JpaRepository<FormField, Long> {

    // 需要过滤基础属性为空的情况（为空表示这些字段不作为用户属性参与计算）
    @Query("select field from FormField field left join fetch field.baseProperty where field.baseForm = :baseForm and field.baseProperty is not null ")
    List<FormField> findAllByBaseFormWithBaseProperty(@Param("baseForm") BaseForm baseForm);

    @Override
    @EntityGraph(attributePaths = {"baseForm", "baseProperty"})
    Page<FormField> findAll(Pageable pageable);

//    @Override
//    @EntityGraph(attributePaths = {"baseForm", "baseProperty"})
//    Optional<FormField> findById(Long aLong);
    @Query("select field from FormField field left join fetch field.baseForm left join fetch field.baseProperty where field.id = :id")
    Optional<FormField> findByIdWithBase(@Param("id") Long id);

    @Query(value = "select field from FormField field left join fetch field.baseProperty left join fetch field.baseForm where field.baseForm.id = :formId",
        countQuery = "select count(field) from FormField field where field.baseForm.id = :formId")
    Page<FormField> findAllByFormId(Pageable pageable, @Param("formId") Long formId);
}
