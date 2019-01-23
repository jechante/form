package com.schinta.repository;

import com.schinta.domain.BaseForm;
import com.schinta.domain.FormField;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the FormField entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormFieldRepository extends JpaRepository<FormField, Long> {

    // 需要过滤基础属性为空的情况（为空表示这些字段不作为用户属性参与计算）
    @Query("select field from FormField field left join fetch field.baseProperty where field.baseForm = :baseForm and field.baseProperty is not null ")
    List<FormField> findAllByBaseFormWithBaseProperty(@Param("baseForm") BaseForm baseForm);
}
