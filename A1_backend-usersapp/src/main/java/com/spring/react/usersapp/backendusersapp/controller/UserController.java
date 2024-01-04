package com.spring.react.usersapp.backendusersapp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.spring.react.usersapp.backendusersapp.model.entity.User;
import com.spring.react.usersapp.backendusersapp.model.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping
    public List<User>list(){
        return this.userService.findAll();
    }

    /* 
    @GetMapping("/{id}")
    public User show(@PathVariable(name="id") Long id){
        Optional<User> userOptional = userService.findById(id); 
        User user = new User();
        if(userOptional.isPresent()){
            user = userOptional.get();
        }
        return user;
    }
    */

    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable(name="id") Long id){
        Optional<User> userOptional = userService.findById(id); 
        if(userOptional.isPresent()){
            return ResponseEntity.ok().body(userOptional.get());
        }
        return ResponseEntity.notFound().build();
    }


    /*
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User user){
        return userService.save(user);
    }
    */

    @PostMapping
    public ResponseEntity<?> create(@RequestBody User user){
        return ResponseEntity.status(HttpStatus.CREATED).body( userService.save(user));
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody User user, @PathVariable Long id){

        Optional<User> o = userService.update(user, id);
        if(o.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(o.orElseThrow());
        }

        return ResponseEntity.notFound().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> remove(@PathVariable Long id){

        Optional<User> o = userService.findById(id);
        if(o.isPresent()){
            userService.removeById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    @Autowired
    private UserService userService;

}
