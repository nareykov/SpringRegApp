package net.nareykov.springregapp.controller;

import net.nareykov.springregapp.model.User;
import net.nareykov.springregapp.service.SecurityService;
import net.nareykov.springregapp.service.UserService;
import net.nareykov.springregapp.validator.UserValidator;
import org.apache.commons.httpclient.methods.PostMethod;
import org.omg.CORBA.NameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by narey on 07.07.2017.
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserValidator userValidator;

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration(Model model) {
        model.addAttribute("userForm", new User());

        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult, Model model) {
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        userService.save(userForm);

        securityService.autoLogin(userForm.getUsername(), userForm.getConfirmPassword());

        return "redirect:/welcome";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, String error, String logout) {
        if (error != null) {
            model.addAttribute("error", "Username or password is incorrect.");
        }

        if (logout != null) {
            model.addAttribute("message", "Logged out successfully.");
        }

        return "login";
    }

    @RequestMapping(value = {"/", "/welcome"}, method = RequestMethod.GET)
    public String welcome(Model model) {
        return "welcome";
    }

    @RequestMapping(value = "/social", method = RequestMethod.POST)
    public String social(@RequestParam("token") String token,  Model model) {
        String server = "http://localhost:8080";
        System.out.println(token);

        /*PostMethod post = new PostMethod(String.format("http://ulogin.ru/token.php?token=%s&host=%s", token, server));
        try {
            System.out.println(post.getResponseBodyAsStream().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        return "welcome";
    }
}