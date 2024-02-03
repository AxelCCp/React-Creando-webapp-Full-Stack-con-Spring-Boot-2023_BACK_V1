package com.spring.react.usersapp.backendusersapp.model.service;

import java.util.List;
import java.util.Optional;

import com.spring.react.usersapp.backendusersapp.model.entity.User;
import com.spring.react.usersapp.backendusersapp.model.request.UserRequest;

public interface UserService {

    List<User> findAll();

    Optional<User>findById(Long id);

    User save(User user); 

    Optional<User>update(UserRequest user, Long id);           //UserRequest : 188

    void removeById(Long id);

}
