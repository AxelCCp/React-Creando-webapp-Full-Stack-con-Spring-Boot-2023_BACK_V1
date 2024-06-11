package com.spring.react.usersapp.backendusersapp.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
//223
public abstract class SimpleGrantedAuthorityJsonCreator {

    @JsonCreator
    public SimpleGrantedAuthorityJsonCreator(@JsonProperty("authority")String role){              //con esta anotacion le decimos q pueble el paramentro con el valor del role.
        
    }

}
