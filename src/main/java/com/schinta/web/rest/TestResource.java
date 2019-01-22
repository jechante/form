package com.schinta.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.schinta.domain.FormSubmit;
import com.schinta.domain.User;
import com.schinta.domain.WxUser;
import com.schinta.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api")
public class TestResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserService userService;


    public TestResource() {
//        this.userService = userService;
//        this.userRepository = userRepository;
//        this.mailService = mailService;
    }

    /**
     * POST  /users  : Creates a new user.
     * <p>
     * Creates a new user if the login and email are not already used, and sends an
     * mail with an activation link.
     * The user needs to be activated on creation.
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new user, or with status 400 (Bad Request) if the login or email is already in use
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/test")
    @Timed
//    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Object> printTest(@Valid @RequestBody Object object) throws URISyntaxException {
        log.info("REST request to save User : {}", object);
//        return ResponseEntity.created(new URI("/api/test/1"))
////            .headers(HeaderUtil.createAlert( "A user is created with identifier " + newUser.getLogin(), newUser.getLogin()))
//            .body(object);
        return new ResponseEntity<>(null, null, HttpStatus.OK);
    }

    @GetMapping("/test1")
    @Timed
    public ResponseEntity test() {
        // 测试group by entity（经测试对mysql可行，因为其实现了SQL99标准）
//        Tuple userSubmitCount = entityManager.createQuery(
//            "select u as user, count(fs) as form_count " +
//                "from WxUser u " +
//                "left join FormSubmit fs on u.id = fs.wxUser.id " +
//                "where u.wxNickName = :name " +
//                "group by u", Tuple.class)
//            .setParameter("name", "jechante")
//            .getSingleResult();
//
//        WxUser wxUser = (WxUser) userSubmitCount.get("user");
//        Assert.isTrue(
//            "jechante".equals(wxUser.getWxNickName())
//        );
//
//        int formCount = (
//            (Number) userSubmitCount.get("form_count")
//        ).intValue();
//        Assert.isTrue(5 == formCount);
//        return new ResponseEntity<>(userSubmitCount, null, HttpStatus.OK);

        User user = userService.getUserWithAuthorities(Long.valueOf(3)).get();
        User user1 = entityManager.createQuery("select user from User user where user.login = :login and user.createdDate = :createdDate", User.class)
            .setParameter("login",user.getLogin())
            .setParameter("createdDate",user.getCreatedDate())
            .getSingleResult();
        return new ResponseEntity<>(user1, null, HttpStatus.OK);

    }
}
