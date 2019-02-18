package com.schinta.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.schinta.domain.Algorithm;
import com.schinta.domain.FormSubmit;
import com.schinta.domain.User;
import com.schinta.domain.WxUser;
import com.schinta.repository.AlgorithmRepository;
import com.schinta.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TestResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserService userService;

    @Autowired
    private AlgorithmRepository algorithmRepository;


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
    @Transactional
    public ResponseEntity test() throws IOException {
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

        // 测试时间参数查询
//        User user = userService.getUserWithAuthorities(Long.valueOf(3)).get();
//        User user1 = entityManager.createQuery("select user from User user where user.login = :login and user.createdDate = :createdDate", User.class)
//            .setParameter("login",user.getLogin())
//            .setParameter("createdDate",user.getCreatedDate())
//            .getSingleResult();
//        return new ResponseEntity<>(user1, null, HttpStatus.OK);

        // 测试不急加载是否能获取代理对象（仅含id）
//        Algorithm algorithm = algorithmRepository.findById((long) 1).get();
//        return new ResponseEntity<>(algorithm, null, HttpStatus.OK);

        // 测试Jackson（支撑转化为Number\String\List\Map等类型）
//        ObjectMapper mapper = new ObjectMapper(); //转换器
//        // OneToMany：数组（取第一个）对数组或者String对数组
//        String string = "13.4";
//        Object result =  mapper.readValue(string, Object.class);
//        return new ResponseEntity<>(result, null, HttpStatus.OK);

        // 测试HashMap的get方法
//        Map map = new HashMap();
//        WxUser user1 = new WxUser();
//        user1.setId("aaa");
//        user1.setWxCountry("test1");
//
//        WxUser user2 = new WxUser();
//        user2.setId("aaa");
//        user2.setWxCountry("test2");
//        map.put(user1,"user1");
//        String test = (String) map.remove(user2);
//        return new ResponseEntity<>(test, null, HttpStatus.OK);

        // 测试entityManager的persist方法
//        Algorithm algorithm = algorithmRepository.findById(Long.valueOf(1)).get();
//        Algorithm algorithm = new Algorithm();
//        algorithm.setEnabled(true);
//        algorithm.setId(2L);
//        entityManager.persist(algorithm);
//        return new ResponseEntity<>(algorithm, null, HttpStatus.OK);

        // 测试localdatetime 与 string 间的转化
        LocalDateTime time = LocalDateTime.now();
        String timeStr = time.toString();
        LocalDateTime time1 = LocalDateTime.parse(timeStr);
        return new ResponseEntity<>(time1, null, HttpStatus.OK);
    }
}
