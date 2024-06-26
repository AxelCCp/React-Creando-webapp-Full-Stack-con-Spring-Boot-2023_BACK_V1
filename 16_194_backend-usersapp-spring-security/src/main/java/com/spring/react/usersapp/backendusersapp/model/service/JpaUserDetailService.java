package com.spring.react.usersapp.backendusersapp.model.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.react.usersapp.backendusersapp.model.dao.UserDao;

@Service
public class JpaUserDetailService implements UserDetailsService{

    //paso 2
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       
        Optional<com.spring.react.usersapp.backendusersapp.model.entity.User> o = this.userDao.getUserByUsername(username);

        if(!o.isPresent()){
            throw new UsernameNotFoundException(String.format("Username:  %s no existe en el sistema!", username));
        }

        com.spring.react.usersapp.backendusersapp.model.entity.User user = o.orElseThrow();

        List<GrantedAuthority> authorities = user.getRoles().stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
        
        return new User(user.getUsername(), user.getPassword(), true, true, true, true, authorities);
    }



    @Autowired
    private UserDao userDao;

    
}
