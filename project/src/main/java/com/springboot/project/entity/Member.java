package com.springboot.project.entity;


import jakarta.persistence.*;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name= "member")
public class Member {

    @Id
    @Column(name = "username")
    private String userName;


    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name="enabled")
    private int enabled;

    @Column(name = "DoB")
    private LocalDate dob;

    @Column(name = "phone")
    private String phone;

    @OneToOne(mappedBy = "member", cascade = {
            CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.REFRESH
    })
    private Role role;

    @OneToMany(mappedBy = "member")
    private List<Transaction> transactions = new ArrayList<>();

    public Member(String userName, String name, String password, LocalDate dob, String phone) {
        this.userName = userName;
        this.name = name;
        this.password = password;
        this.dob = dob;
        this.phone = phone;
        enabled = 1;
        role = new Role("ACTIVE", this);
    }

    public Member() {
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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

    public void addTransaction(Transaction transaction){
        transactions.add(transaction);
    }

    @Override
    public String toString() {
        return "Member{" +
                "userName='" + userName + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", enabled=" + enabled +
                ", dob=" + dob +
                ", phone='" + phone + '\'' +
                ", role=" + role +
                ", transactions=" + transactions +
                '}';
    }
}
