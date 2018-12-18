package com.schinta.repository;

import com.schinta.domain.UserMatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the UserMatch entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserMatchRepository extends JpaRepository<UserMatch, Long> {

    @Query(value = "select distinct user_match from UserMatch user_match left join fetch user_match.pushRecords",
        countQuery = "select count(distinct user_match) from UserMatch user_match")
    Page<UserMatch> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct user_match from UserMatch user_match left join fetch user_match.pushRecords")
    List<UserMatch> findAllWithEagerRelationships();

    @Query("select user_match from UserMatch user_match left join fetch user_match.pushRecords where user_match.id =:id")
    Optional<UserMatch> findOneWithEagerRelationships(@Param("id") Long id);

}