package com.spring.react.usersapp.backendusersapp.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.react.usersapp.backendusersapp.model.IUser;
import com.spring.react.usersapp.backendusersapp.model.dao.RoleDao;
import com.spring.react.usersapp.backendusersapp.model.dao.UserDao;
import com.spring.react.usersapp.backendusersapp.model.dto.UserDto;
import com.spring.react.usersapp.backendusersapp.model.dto.mapper.DtoMapperUser;
import com.spring.react.usersapp.backendusersapp.model.entity.Role;
import com.spring.react.usersapp.backendusersapp.model.entity.User;
import com.spring.react.usersapp.backendusersapp.model.request.UserRequest;

@Service
public class UserServiceImpl implements UserService{

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        List<User>users = (List<User>) this.userDao.findAll();
        return users.stream().map(u -> DtoMapperUser.builder().setUser(u).build()).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDto> findAll(Pageable pageable) {
        Page<User>usersPage = this.userDao.findAll(pageable);
        return usersPage.map(u -> DtoMapperUser.builder().setUser(u).build());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto> findById(Long id) {

        //Forma 1
        /*Optional<User> op = userDao.findById(id);
        if(op.isPresent()){
            return Optional.of(DtoMapperUser.builder().setUser(op.orElseThrow()).build());
        }
        return Optional.empty();*/
        
        //Forma 2
        return userDao.findById(id).map(u -> DtoMapperUser.builder().setUser(u).build());

    }

    @Override
    @Transactional
    public UserDto save(User user) {
        String passwordBCrypt = passwordEncoder.encode(user.getPassword());
        user.setPassword(passwordBCrypt);
        List<Role> roles = this.getRoles(user);
        user.setRoles(roles);
        return DtoMapperUser.builder().setUser(userDao.save(user)).build();  
    }

    /*FORMA 1
    @Override
    public Optional<User> update(User user, Long id) {

        Optional<User> o = this.findById(id);
        if(o.isPresent()){
            User userDb = o.orElseThrow();
            userDb.setUsername(user.getUsername());
            userDb.setEmail(user.getEmail());
            return Optional.of(this.save(userDb));
        }

        return Optional.empty();
    }
    */

    //FORMA 2
    @Override
    @Transactional              //UserRequest : 188
    public Optional<UserDto> update(UserRequest user, Long id) {

        Optional<User> o = userDao.findById(id);

        User userOp = null;

        if(o.isPresent()){
            List<Role> roles = this.getRoles(user);
            User userDb = o.orElseThrow();
            userDb.setRoles(roles);
            userDb.setUsername(user.getUsername());
            userDb.setEmail(user.getEmail());
            userOp = userDao.save(userDb);
        }

        return Optional.ofNullable(DtoMapperUser.builder().setUser(userOp).build());
    }


    @Override
    @Transactional
    public void removeById(Long id) {
        userDao.deleteById(id);
    }


    private List<Role> getRoles(IUser user) {

        Optional<Role>ou = this.roleDao.findByName("ROLE_USER");

        List<Role>roles = new ArrayList<>();

        if(ou.isPresent()){
            roles.add(ou.orElseThrow());
        }

        if(user.isAdmin()){
            Optional<Role> oa = roleDao.findByName("ROLE_ADMIN");
            if(oa.isPresent()){
                roles.add(oa.orElseThrow());
            }
        }

        return roles;
    }


    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired 
    private RoleDao roleDao;

   
}
