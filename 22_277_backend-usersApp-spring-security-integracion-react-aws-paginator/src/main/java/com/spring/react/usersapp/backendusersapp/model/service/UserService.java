package com.spring.react.usersapp.backendusersapp.model.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.spring.react.usersapp.backendusersapp.model.dto.UserDto;
import com.spring.react.usersapp.backendusersapp.model.entity.User;
import com.spring.react.usersapp.backendusersapp.model.request.UserRequest;

public interface UserService {

    List<UserDto> findAll();

    Page<UserDto>findAll(Pageable pageable);

    Optional<UserDto>findById(Long id);

    UserDto save(User user); 

    Optional<UserDto>update(UserRequest user, Long id);           //UserRequest : 188

    void removeById(Long id);

}
