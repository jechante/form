package com.schinta.repository;

import com.schinta.domain.UserProperty;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the UserProperty entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserPropertyRepository extends JpaRepository<UserProperty, Long> {

}
