package com.schinta.repository;

import com.schinta.domain.MatchRecord;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the MatchRecord entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MatchRecordRepository extends JpaRepository<MatchRecord, Long> {

}
