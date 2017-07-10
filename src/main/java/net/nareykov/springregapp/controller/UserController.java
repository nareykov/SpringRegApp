package net.nareykov.springregapp.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.nareykov.springregapp.model.User;
import net.nareykov.springregapp.service.SecurityService;
import net.nareykov.springregapp.service.UserService;
import net.nareykov.springregapp.validator.UserValidator;
import org.apache.commons.httpclient.methods.PostMethod;
import org.omg.CORBA.NameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.WebRequest;

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

    @RequestMapping(value = "/social")
    public String social(WebRequest request, @RequestParam("token") String token, Model model) {
        String server = request.getHeader("host");
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://ulogin.ru/token.php?token=" + token + "&host=" + server;
        parseJson(restTemplate.getForEntity(url, String.class), model);
        return "welcome";
    }

    private void parseJson(ResponseEntity<String> response,  Model model) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = null;
        try {
            root = mapper.readTree(response.getBody());
        } catch (IOException e) {
            e.printStackTrace();
        }
        model.addAttribute("username", root.get("first_name").asText() + ' ' + root.get("last_name").asText());
        model.addAttribute("network", root.get("network").asText());
    }
}