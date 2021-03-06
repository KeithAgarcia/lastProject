package com.theironyard.lastProject.controllers;

import com.theironyard.lastProject.entities.User;
import com.theironyard.lastProject.repositories.MealRepository;
import com.theironyard.lastProject.repositories.UserRepository;
import com.theironyard.lastProject.services.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Keith on 6/3/17.
 */
@RestController
public class UserController {
    UserService userService;
    UserRepository users;
    MealRepository meals;

    public UserController(UserService userService, UserRepository users) {
        this.users = users;
        this.userService = userService;
    }

    @PostConstruct
    public void init() {
        if (users.count() == 0) {
            userService.createUser("Keith", "admin","704-578-5872", 5, 0.0, 3, 0);
            userService.createUser("Neilson", "admin", "704-576-7734", 5, 0.0, 3, 0);
            userService.createUser("Mac", "admin", "704-444-1068", 5, 0.0, 5, 0);
        }
    }


    @CrossOrigin
    @RequestMapping(path = "/new-user", method = RequestMethod.POST)
    public void newUser(@RequestBody User user, HttpServletResponse response) {
        try {
            userService.createUser(user.getUsername(), user.getPassword(), user.getPassword(), false, user.getPhone(),
                    user.getToken(), user.getRating(), user.getTotalRatings(), user.getTotalCookMeals());
        } catch (IllegalArgumentException ex) {
            response.setStatus(422);
        }
    }

    @CrossOrigin
    @RequestMapping(path = "/select-user/{id}", method = RequestMethod.GET)
    public User selectUser(@PathVariable("id") int id){
        return users.findOne(id);
    }

    @CrossOrigin
    @RequestMapping(path = "/rate-user/{id}", method = RequestMethod.PUT)
    public void rateUser(@PathVariable("id") int id, @RequestBody int rating) {
        User ratee = users.findOne(id);
        userService.rateUser(ratee, rating);
    }

    @CrossOrigin
    @RequestMapping(path = "/userInfo", method = RequestMethod.GET)
    public User userInfo(){
        org.springframework.security.core.userdetails.User auth = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User u = users.findFirstByUsername(auth.getUsername());
        return users.findOne(u.getId());
    }

}
