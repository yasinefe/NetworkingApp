package com.networkingapp.controller;

import com.networkingapp.domain.Message;
import com.networkingapp.exception.UserNotFoundException;
import com.networkingapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/{user}/message", method = POST)
    public ResponseEntity postMessage(@PathVariable(value = "user") String user, @RequestBody @Valid Message message) {
        userService.post(user, message.getMessage());
        return ResponseEntity.status(CREATED).build();
    }

    @RequestMapping(value = "/{user}/follow/{followingUser}", method = PUT)
    public ResponseEntity follow(@PathVariable(value = "user") String user,
                                 @PathVariable(value = "followingUser") String followingUser) throws UserNotFoundException {
        userService.follow(user, followingUser);
        return ResponseEntity.accepted().build();
    }

    @RequestMapping(value = "/{user}/wall", method = GET)
    public ResponseEntity wall(@PathVariable(value = "user") String user) throws UserNotFoundException {
        return ResponseEntity.ok(userService.getWall(user));
    }

    @RequestMapping(value = "/{user}/timeline", method = GET)
    public ResponseEntity timeline(@PathVariable(value = "user") String user) throws UserNotFoundException {
        return ResponseEntity.ok(userService.getTimeline(user));
    }
}
