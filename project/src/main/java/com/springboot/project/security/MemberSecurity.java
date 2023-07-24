package com.springboot.project.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.sql.DataSource;

@Configuration
public class MemberSecurity {

    private final AuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Autowired
    public MemberSecurity(AuthenticationSuccessHandler customAuthenticationSuccessHandler) {
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
    }
    @Bean
    public UserDetailsManager userDetailsManager(DataSource datasource){
        var jdbcUserDetailsManager =  new JdbcUserDetailsManager(datasource);
        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "SELECT username, password, enabled " +
                        "FROM member " +
                        "WHERE username = ?"
        );
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                "SELECT member, role " +
                        "FROM roles " +
                        "WHERE member = ?"
        );
        return jdbcUserDetailsManager;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests(configurer-> configurer

                        .requestMatchers("/").permitAll()
                        .requestMatchers("/active/**").hasAuthority("ACTIVE")
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
                        .requestMatchers("/show-signup-page").permitAll()
                        .requestMatchers("/register-user").permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .formLogin(form -> form
                        .successHandler(customAuthenticationSuccessHandler)
                        .loginPage("/show-login-page")
                        .loginProcessingUrl("/authenticate-the-user")
                        .permitAll()
                )
                .logout(LogoutConfigurer::permitAll)
                .exceptionHandling(configurer->configurer.accessDeniedPage("/access-denied")
                );
        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
