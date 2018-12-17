package com.schinta.repository;

import com.schinta.domain.WxUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the WxUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WxUserRepository extends JpaRepository<WxUser, Long> {

}
