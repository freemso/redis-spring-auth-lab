package edu.fudan.rest;

import edu.fudan.annotation.Authorization;
import edu.fudan.annotation.CurrentUser;
import edu.fudan.domain.User;
import edu.fudan.dto.RegisterReq;
import edu.fudan.model.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@CrossOrigin
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * User registration request.
     *
     * @param registerReq, required createUser form data
     * @return user private DTO with email field
     */
    @PostMapping
    ResponseEntity<User> register(@Valid @RequestBody RegisterReq registerReq) {
        User user = this.userService.createUser(registerReq.getEmail(),
                registerReq.getName(), registerReq.getPassword());
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    /**
     * Get meta data of the user with id equals to {uid}.
     * Only authorized currentUser with the same id as {uid} could get
     * user private data. Others can't.
     *
     * @param uid, id of the user to get
     * @return user private data
     */
    @GetMapping("/{uid}")
    @Authorization
    ResponseEntity<User> getUserPrivate(@CurrentUser User currentUser, @PathVariable long uid) {
        User user = this.userService.getUserPrivate(currentUser, uid);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}
