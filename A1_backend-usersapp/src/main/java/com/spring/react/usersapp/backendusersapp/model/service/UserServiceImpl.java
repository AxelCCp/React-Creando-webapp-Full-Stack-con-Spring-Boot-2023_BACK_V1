package com.spring.react.usersapp.backendusersapp.model.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.react.usersapp.backendusersapp.model.dao.UserDao;
import com.spring.react.usersapp.backendusersapp.model.entity.User;

@Service
public class UserServiceImpl implements UserService{

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return (List<User>) this.userDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userDao.findById(id);
    }

    @Override
    @Transactional
    public User save(User user) {
        return userDao.save(user);
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
    @Transactional
    public Optional<User> update(User user, Long id) {

        Optional<User> o = this.findById(id);

        User userOp = null;

        if(o.isPresent()){
            User userDb = o.orElseThrow();
            userDb.setUsername(user.getUsername());
            userDb.setEmail(user.getEmail());
            userOp = this.save(userDb);
        }

        return Optional.ofNullable(userOp);
    }


    @Override
    @Transactional
    public void removeById(Long id) {
        userDao.deleteById(id);
    }

    @Autowired
    private UserDao userDao;

}
