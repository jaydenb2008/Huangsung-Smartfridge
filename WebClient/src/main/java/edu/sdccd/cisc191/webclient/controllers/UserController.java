package edu.sdccd.cisc191.webclient.controllers;

import edu.sdccd.cisc191.webclient.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String viewHomePage(Model model) {
        model.addAttribute("alluserlist", userService.getAllUsers());
        return "index";
    }
}