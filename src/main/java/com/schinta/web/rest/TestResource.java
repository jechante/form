//package com.schinta.web.rest;
//
//import com.codahale.metrics.annotation.Timed;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.validation.Valid;
//import java.net.URI;
//import java.net.URISyntaxException;
//
//@RestController
//@RequestMapping("/api")
//public class TestResource {
//
//    private final Logger log = LoggerFactory.getLogger(UserResource.class);
//
//
//
//
//    public TestResource() {
////        this.userService = userService;
////        this.userRepository = userRepository;
////        this.mailService = mailService;
//    }
//
//    /**
//     * POST  /users  : Creates a new user.
//     * <p>
//     * Creates a new user if the login and email are not already used, and sends an
//     * mail with an activation link.
//     * The user needs to be activated on creation.
//     *
//     * @param userDTO the user to create
//     * @return the ResponseEntity with status 201 (Created) and with body the new user, or with status 400 (Bad Request) if the login or email is already in use
//     * @throws URISyntaxException if the Location URI syntax is incorrect
//     * @throws BadRequestAlertException 400 (Bad Request) if the login or email is already in use
//     */
//    @PostMapping("/test")
//    @Timed
////    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
//    public ResponseEntity<Object> printTest(@Valid @RequestBody Object object) throws URISyntaxException {
//        log.info("REST request to save User : {}", object);
////        return ResponseEntity.created(new URI("/api/test/1"))
//////            .headers(HeaderUtil.createAlert( "A user is created with identifier " + newUser.getLogin(), newUser.getLogin()))
////            .body(object);
//        return new ResponseEntity<>(null, null, HttpStatus.OK);
//    }
//
//
//}
