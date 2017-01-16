package de.lmu.controller;

import de.lmu.domain.User;
import de.lmu.service.UserService;
import org.hibernate.validator.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by robertMueller on 03.01.17.
 */
@RestController
@RequestMapping(UserController.USER_BASE_URL)
public class UserController {

    @Autowired
    private UserService userService;
    public static final String USER_BASE_URL = "/user";

    @RequestMapping(
            value=USER_BASE_URL + "/all",
            method=RequestMethod.GET,
            produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<User>> getUsers(){
        return new ResponseEntity<Collection<User>>(userService.getUsers(), HttpStatus.OK);
    }

    @RequestMapping(
            value=USER_BASE_URL + "/{email}",
            method=RequestMethod.GET,
            produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUser(@PathVariable("email") String email ){
        User user = userService.getUser(email);
        if(user != null){
            return new ResponseEntity<User>(user, HttpStatus.OK);
        }
        return new ResponseEntity<User>(HttpStatus.NOT_FOUND);

    }

    @RequestMapping(value = USER_BASE_URL ,
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createUser(@RequestBody User user){
        userService.save(user);
        // send saved User object back and possibly send new information on that object, for example the user id
        return new ResponseEntity<User>(user, HttpStatus.CREATED);
    }

    @RequestMapping(value=USER_BASE_URL + "/{email}",
            method = RequestMethod.DELETE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> deleteUser(@PathVariable String email, @RequestBody User user) {
        //delete logic to come
        userService.delete(email);
        /*
        if(deleted){
            return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<User>(HttpStatus.INTERNAL_SERVER_ERROR);
        }*/
        return new ResponseEntity<User>(HttpStatus.OK);

    }
}
