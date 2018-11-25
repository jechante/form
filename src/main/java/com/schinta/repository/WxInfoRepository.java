package com.schinta.repository;

import com.schinta.domain.WxInfo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the WxInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WxInfoRepository extends JpaRepository<WxInfo, Long> {

}
