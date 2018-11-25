package com.schinta.repository;

import com.schinta.domain.UserBase;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the UserBase entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserBaseRepository extends JpaRepository<UserBase, Long> {

}
