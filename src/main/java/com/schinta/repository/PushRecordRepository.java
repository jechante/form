package com.schinta.repository;

import com.schinta.domain.PushRecord;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PushRecord entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PushRecordRepository extends JpaRepository<PushRecord, Long> {

}
