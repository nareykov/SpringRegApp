package net.nareykov.springregapp.service;

import net.nareykov.springregapp.model.User;

/**
 * Created by narey on 06.07.2017.
 */
public interface UserService {

    void save(User user);

    User findByUsername(String username);
}
