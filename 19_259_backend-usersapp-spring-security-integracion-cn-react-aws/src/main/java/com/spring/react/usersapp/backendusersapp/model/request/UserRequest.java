package com.spring.react.usersapp.backendusersapp.model.request;

import com.spring.react.usersapp.backendusersapp.model.IUser;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

//UserRequest : 188

public class UserRequest implements IUser {

    @NotBlank (message = "El username no puede ser vacio")
    @Size(min=4, max = 10)
    private String username;
    
     @Email
    @NotEmpty
    private String email;

    private boolean admin;

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    @Override
    public boolean isAdmin() {
        return admin;
    }
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    
    

}
