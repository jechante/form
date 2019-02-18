package com.schinta.repository;

import com.schinta.domain.WxUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the WxUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WxUserRepository extends JpaRepository<WxUser, String> {
    @Query("select user.id from WxUser user where user.userStatus = 'ACTIVE'")
    List<String> getAllActiveUserOpenIds();
}
