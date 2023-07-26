package com.springboot.project.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/show-login-page")
    public String showLoginPage(@RequestParam(value = "errorMessage", required = false) String error, Model model){
        model.addAttribute("ErrorMessage", error);
        return "login";
    }

    @GetMapping("/access-denied")
    public String  showAccessDenied(){
        return "access-denied";
    }

}
