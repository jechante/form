package com.schinta.repository;

import com.schinta.domain.UserProperty;
import com.schinta.domain.WxUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the UserProperty entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserPropertyRepository extends JpaRepository<UserProperty, Long> {
    List<UserProperty> findAllByWxUser(WxUser user);
}
