package com.spring.react.usersapp.backendusersapp.model.dao;

import org.springframework.data.repository.CrudRepository;

import com.spring.react.usersapp.backendusersapp.model.entity.User;

public interface UserDao extends CrudRepository<User, Long>{}
