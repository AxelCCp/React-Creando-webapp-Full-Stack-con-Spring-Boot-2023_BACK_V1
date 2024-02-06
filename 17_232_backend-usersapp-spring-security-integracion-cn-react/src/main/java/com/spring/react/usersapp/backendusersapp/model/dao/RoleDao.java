package com.spring.react.usersapp.backendusersapp.model.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.spring.react.usersapp.backendusersapp.model.entity.Role;

public interface RoleDao extends CrudRepository<Role, Long>{ 

    Optional<Role> findByName(String role);
    
}
