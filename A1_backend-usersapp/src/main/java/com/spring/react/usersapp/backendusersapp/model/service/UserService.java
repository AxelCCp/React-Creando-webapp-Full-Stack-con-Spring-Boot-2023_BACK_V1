package com.spring.react.usersapp.backendusersapp.model.service;

import java.util.List;
import java.util.Optional;

import com.spring.react.usersapp.backendusersapp.model.entity.User;

public interface UserService {

    List<User> findAll();

    Optional<User>findById(Long id);

    User save(User user); 

    Optional<User>update(User user, Long id);

    void removeById(Long id);

}
