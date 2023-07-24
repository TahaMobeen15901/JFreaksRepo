package com.springboot.project.security;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            String role = authority.getAuthority();
            if (role.equals("ADMIN")) {
                response.sendRedirect("/admin/home"); // Redirect admin to admin home page
                return;
            } else if (role.equals("ACTIVE")) {
                response.sendRedirect("/active/home"); // Redirect active user to active home page
                return;
            }
        }

        // Handle other roles or unknown roles here
        response.sendRedirect("/access-denied");
    }
}
