package net.nareykov.springregapp.dao;

import net.nareykov.springregapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by narey on 06.07.2017.
 */
public interface UserDao extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
