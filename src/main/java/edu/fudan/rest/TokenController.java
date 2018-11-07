package edu.fudan.rest;


import edu.fudan.annotation.Authorization;
import edu.fudan.annotation.CurrentUser;
import edu.fudan.domain.User;
import edu.fudan.dto.LoginReq;
import edu.fudan.model.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/token")
public class TokenController {

    private final UserService userService;

    @Autowired
    public TokenController(UserService userService) {
        this.userService = userService;
    }

    /**
     * User createToken request.
     *
     * @param loginReq, required createToken form data
     * @return createToken response DTO
     */
    @PostMapping
    ResponseEntity<String> login(@Valid @RequestBody LoginReq loginReq) {
        String authenticationResp = userService.createToken(
                loginReq.getEmail(), loginReq.getPassword());

        return new ResponseEntity<>(authenticationResp, HttpStatus.CREATED);
    }

    /**
     * User deleteToken request.
     *
     * @param currentUser, authorized current user
     * @return response with empty body
     */
    @DeleteMapping
    @Authorization
    ResponseEntity logout(@CurrentUser User currentUser) {
        userService.deleteToken(currentUser.getUserId());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
