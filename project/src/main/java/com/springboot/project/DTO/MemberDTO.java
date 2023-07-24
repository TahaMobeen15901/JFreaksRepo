package com.springboot.project.DTO;


import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class MemberDTO {

    @NotNull
    private String userName;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z]+( [a-zA-Z]*)?$")
    private String name;


    @NotNull
    @Size(min = 16)
    private String password;


    private int enabled;


    private LocalDate dob;


    @NotNull
    @Pattern(regexp = "^((\\+92([- ])?\\d{3}([- ])?\\d{7})||(0\\d{3}([- ])?\\d{7}))$")
    private String phone;

    public MemberDTO(String userName, String name, String password, int enabled, LocalDate dob, String phone) {
        this.userName = userName;
        this.name = name;
        this.password = password;
        this.enabled = enabled;
        this.dob = dob;
        this.phone = phone;
    }

    public MemberDTO(String userName, String name, String password, LocalDate dob, String phone) {
        this.userName = userName;
        this.name = name;
        this.password = password;
        this.dob = dob;
        this.phone = phone;
        enabled = 1;
    }

    public MemberDTO() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
