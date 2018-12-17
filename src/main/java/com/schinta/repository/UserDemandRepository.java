package com.schinta.repository;

import com.schinta.domain.UserDemand;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the UserDemand entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserDemandRepository extends JpaRepository<UserDemand, Long> {

}
