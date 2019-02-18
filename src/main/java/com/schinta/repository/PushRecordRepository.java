package com.schinta.repository;

import com.schinta.domain.PushRecord;
import com.schinta.domain.WxUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the PushRecord entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PushRecordRepository extends JpaRepository<PushRecord, Long> {

    @Query("select push from PushRecord push left join fetch push.userMatches left join fetch push.user where push.id = :id")
    Optional<PushRecord> findWithMatchesAndUser(@Param("id") Long id);

    @Query("select push from PushRecord push left join fetch push.userMatches left join fetch push.user where push.user.id = :id and push.pushDateTime = :localDateTime")
    Optional<PushRecord> findAllByUserIdAndPushDateTimeWithMatchesAndUser(@Param("id") String id, @Param("localDateTime")LocalDateTime localDateTime);
}
