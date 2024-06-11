    package com.spring.react.usersapp.backendusersapp.model.dto.mapper;

import com.spring.react.usersapp.backendusersapp.model.dto.UserDto;
import com.spring.react.usersapp.backendusersapp.model.entity.User;

public class DtoMapperUser {

    //constructor privado para q no se pueda instanciar

    private DtoMapperUser (){}

    public static DtoMapperUser builder() {
        return new DtoMapperUser();
    }

    
    public DtoMapperUser setUser(User user) {
        this.user = user;
        return this;
    }

    public UserDto build(){
        
        boolean isAdmin = user.getRoles().stream().anyMatch(r -> "ROLE_ADMIN".equals(r.getName()));

        if(user == null){
            throw new RuntimeException("Debe pasar el entity user!");
        }
        return new UserDto(this.user.getId(), user.getUsername(), user.getEmail(), isAdmin);
    }

    
    private User user;

}
