package de.lmu.service.impl;

import de.lmu.domain.User;
import de.lmu.repository.UserRepository;
import de.lmu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robertMueller on 03.01.17.
 */
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUser(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User save(User user) {
        User saved = userRepository.save(user);
        return saved;
    }

    public void delete(String email) {
        userRepository.deleteByEmail(email);
    }
}
