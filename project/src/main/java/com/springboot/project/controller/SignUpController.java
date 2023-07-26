package com.springboot.project.controller;

import com.springboot.project.DTO.MemberDTO;
import com.springboot.project.entity.Member;
import com.springboot.project.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SignUpController {

    MemberService memberService;


    @Autowired
    public SignUpController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/show-signup-page")
    public String signUp(Model model){
        MemberDTO member = new MemberDTO();
        model.addAttribute("Member", member);
        return "signup";
    }

    @PostMapping("/register-user")
    public String registration(@Valid @ModelAttribute("Member") MemberDTO memberDTO, BindingResult result, Model model){
        Member existing = memberService.findByUserName(memberDTO.getUserName());
        if((existing != null) && (existing.getUserName() != null) && !(existing.getUserName().isEmpty())){
            result.rejectValue("userName", null, "There is already an account registered with the same username");
        }

        if(result.hasErrors()){
            model.addAttribute("Member", memberDTO);
            return "signup";
        }
        try{
            memberService.saveMember(memberDTO);
        } catch (Exception e){
            return "redirect:/show-login-page?errorMessage=Error: Can't Sign Up right now!";
        }

        return "redirect:/show-login-page?success=true";
    }

}
