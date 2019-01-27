package com.schinta.repository;

import com.schinta.domain.UserDemand;
import com.schinta.domain.WxUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the UserDemand entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserDemandRepository extends JpaRepository<UserDemand, Long> {

    @Query("select demand from UserDemand demand left join fetch demand.wxUser user where user.userStatus = 'ACTIVE'")
    List<UserDemand> findAllActiveWithUser();

    List<UserDemand> findAllByWxUser(WxUser user);
}
