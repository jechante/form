package com.schinta.repository;

import com.schinta.domain.Algorithm;
import com.schinta.domain.UserMatch;
import com.schinta.domain.WxUser;
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

    // 查询某个算法下某个用户的所有匹配结果
    @Query("select user_match from UserMatch user_match where (user_match.userA =:user or user_match.userB = :user) and user_match.algorithm = :algorithm")
    List<UserMatch> findAllByWxUserAndAlgorithm(@Param("user")WxUser user, @Param("algorithm")Algorithm algorithm);

    // 查询某个算法下全部激活用户的匹配结果
    @Query("select user_match from UserMatch user_match where user_match.algorithm = :algorithm and user_match.userA.userStatus = 'ACTIVE' and user_match.userB.userStatus = 'ACTIVE'")
    List<UserMatch> findAllByAlgorithm(@Param("algorithm")Algorithm algorithm);

    // 查询某个算法下某个用户未被推送的匹配（仅激活状态用户）
    @Query("select user_match from UserMatch user_match where ( user_match.userA =:user and (user_match.pushStatus is null or user_match.pushStatus = 'B' ) and user_match.userB.userStatus = 'ACTIVE' ) or (user_match.userB = :user and (user_match.pushStatus is null or user_match.pushStatus = 'A' ) and user_match.userA.userStatus = 'ACTIVE' ) and user_match.algorithm = :algorithm")
    List<UserMatch> findUnPushedByWxUserAndAlgorithm(@Param("user")WxUser user, @Param("algorithm")Algorithm algorithm);

    // 查询某个算法下所有未被推送的匹配（仅激活状态用户）
    // 重要：null不参与<>计算，因此需要增加or user_match.pushStatus is null
    @Query("select user_match from UserMatch user_match where (user_match.pushStatus <> 'BOTH' or user_match.pushStatus is null ) and user_match.algorithm = :algorithm and user_match.userA.userStatus = 'ACTIVE' and user_match.userB.userStatus = 'ACTIVE'")
    List<UserMatch> findUnPushedByAlgorithm(@Param("algorithm")Algorithm algorithm);

    @EntityGraph(attributePaths = {"algorithm","userA","userB"})
    Page<UserMatch> findAll(Pageable pageable);

    // 查询用户的历史匹配记录（推送给任意一方用户）
    @Query("select user_match from UserMatch user_match left join fetch user_match.userA left join fetch user_match.userB where user_match.pushStatus is not null and user_match.algorithm = :algorithm and (user_match.userA.id = :openId or user_match.userB.id = :openId) order by user_match.scoreTotal desc ")
    List<UserMatch> findUserPushed(@Param("openId") String openId, @Param("algorithm") Algorithm algorithm);

    @Query("select user_match from UserMatch user_match left join fetch user_match.userA left join fetch user_match.userB where user_match.id =:id")
    Optional<UserMatch> findOneWithUsers(@Param("id") Long id);

}
