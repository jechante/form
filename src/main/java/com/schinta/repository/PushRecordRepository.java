package com.schinta.repository;

import com.schinta.domain.PushRecord;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the PushRecord entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PushRecordRepository extends JpaRepository<PushRecord, Long> {

    @Query("select push from PushRecord push left join fetch push.userMatches where push.id = :id")
    Optional<PushRecord> findWithMatches(@Param("id") Long id);
}
