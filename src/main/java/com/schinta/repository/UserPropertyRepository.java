package com.schinta.repository;

import com.schinta.domain.UserProperty;
import com.schinta.domain.WxUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;


/**
 * Spring Data  repository for the UserProperty entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserPropertyRepository extends JpaRepository<UserProperty, Long> {
    List<UserProperty> findAllByWxUser(WxUser user);

    @Query("select property from UserProperty property join property.wxUser user where user.userStatus = 'ACTIVE'") // 这里仅join，没有fetch，因为只需要用户的id和基础属性的id即可
    List<UserProperty> findAllActiveWithUser();

    List<UserProperty> findAllByWxUserIn(Set<WxUser> userSet);

    @Query(value = "select property from UserProperty property left join fetch property.wxUser where property.base.propertyName = '头像'",
        countQuery = "select count(property) from UserProperty property where property.base.propertyName = '头像'")
    Page<UserProperty> findAllUserPictures(Pageable pageable);

    @Modifying
    @Query("delete from UserProperty property where property.wxUser = :wxUser")
    int deleteAllByWxUser(@Param("wxUser") WxUser wxUser);

}
