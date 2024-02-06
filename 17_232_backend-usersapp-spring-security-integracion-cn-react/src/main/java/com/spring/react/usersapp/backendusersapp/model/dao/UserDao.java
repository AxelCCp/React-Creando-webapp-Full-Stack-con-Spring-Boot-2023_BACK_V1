package com.spring.react.usersapp.backendusersapp.model.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.spring.react.usersapp.backendusersapp.model.entity.User;

public interface UserDao extends CrudRepository<User, Long>{

    Optional<User> findByUsername(String username);

    //JQL o HQL query language.
    @Query("select u from User u where u.username=?1")
    Optional<User> getUserByUsername(String username);
}
