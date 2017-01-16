package de.lmu.service;

import de.lmu.domain.User;
import org.springframework.data.jpa.repository.Modifying;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robertMueller on 03.01.17.
 */
public interface UserService {
    List<User> getUsers();
    User getUser(String email);
    User save(User user);
    void delete(String email);
}
