package com.springboot.project.entity;


import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "trans_action")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "book")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "member")
    private Member member;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Column(name = "dot")
    private LocalDate dateOfTransaction;

    public Transaction(Book book, Member member, TransactionType type) {
        this.book = book;
        this.member = member;
        dateOfTransaction = LocalDate.now();
        this.type = type;
    }

    public Transaction() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public LocalDate getDateOfTransaction() {
        return dateOfTransaction;
    }

    public void setDateOfTransaction(LocalDate dateOfTransaction) {
        this.dateOfTransaction = dateOfTransaction;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", book=" + book.getIsbn() +
                ", member=" + member.getUserName() +
                ", type=" + type +
                ", dateOfTransaction=" + dateOfTransaction +
                '}';
    }
}