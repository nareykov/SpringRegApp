package net.nareykov.springregapp.service;

/**
 * Created by narey on 06.07.2017.
 */
public interface SecurityService {

    String findLoggedInUsername();

    void autoLogin(String username, String password);
}
