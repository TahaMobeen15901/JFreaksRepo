package com.springboot.project.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @GetMapping("/show-login-page")
    public String showLoginPage(){
        return "login";
    }

    @GetMapping("/access-denied")
    public String  showAccessDenied(){
        return "access-denied";
    }

}
