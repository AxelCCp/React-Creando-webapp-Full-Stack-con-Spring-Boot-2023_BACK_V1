package com.spring.react.usersapp.backendusersapp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.spring.react.usersapp.backendusersapp.model.request.UserRequest;
import com.spring.react.usersapp.backendusersapp.model.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
@CrossOrigin(originPatterns = "*")
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
    public ResponseEntity<?> create(@Valid @RequestBody User user, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return validation(bindingResult);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body( userService.save(user));
    }


    @PutMapping("/{id}")                                //UserRequest : 188
    public ResponseEntity<?> update(@Valid @RequestBody UserRequest user, BindingResult bindingResult, @PathVariable Long id){
        if(bindingResult.hasErrors()){
            return validation(bindingResult);
        }
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


    private ResponseEntity<?> validation(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        bindingResult.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
            
        });
        return ResponseEntity.badRequest().body(errors);
    }


    @Autowired
    private UserService userService;

}
