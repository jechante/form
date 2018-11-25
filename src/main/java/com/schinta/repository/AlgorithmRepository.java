package com.schinta.repository;

import com.schinta.domain.Algorithm;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Algorithm entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlgorithmRepository extends JpaRepository<Algorithm, Long> {

}
