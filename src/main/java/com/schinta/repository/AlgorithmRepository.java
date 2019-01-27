package com.schinta.repository;

import com.schinta.domain.Algorithm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Algorithm entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlgorithmRepository extends JpaRepository<Algorithm, Long> {

    @Query(value = "select distinct algorithm from Algorithm algorithm left join fetch algorithm.filterProperties left join fetch algorithm.scoreProperties",
        countQuery = "select count(distinct algorithm) from Algorithm algorithm")
    Page<Algorithm> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct algorithm from Algorithm algorithm left join fetch algorithm.filterProperties left join fetch algorithm.scoreProperties")
    List<Algorithm> findAllWithEagerRelationships();

    @Query("select algorithm from Algorithm algorithm left join fetch algorithm.filterProperties left join fetch algorithm.scoreProperties where algorithm.id =:id")
    Optional<Algorithm> findOneWithEagerRelationships(@Param("id") Long id);

    @Query("select algorithm from Algorithm algorithm left join fetch algorithm.filterProperties left join fetch algorithm.scoreProperties where algorithm.enabled = true ")
    Optional<Algorithm> findEnabledOneWithEagerRelationships();

}
