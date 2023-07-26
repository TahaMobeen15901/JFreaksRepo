package com.springboot.project.DTO;


import com.springboot.project.entity.TransactionType;

import java.time.LocalDate;

public class FineDTO {

    private long id;
    private int book;

    private String member;


    private TransactionType type;

    private LocalDate dateOfTransaction;

    private double fine;

    public FineDTO(long id, int book, String member, TransactionType type, LocalDate dateOfTransaction, double fine) {
        this.id = id;
        this.book = book;
        this.member = member;
        this.type = type;
        this.dateOfTransaction = dateOfTransaction;
        this.fine = fine;
    }

    public FineDTO() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getBook() {
        return book;
    }

    public void setBook(int book) {
        this.book = book;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
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

    public double getFine() {
        return fine;
    }

    public void setFine(double fine) {
        this.fine = fine;
    }

    @Override
    public String toString() {
        return "FineDTO{" +
                "id=" + id +
                ", book=" + book +
                ", member='" + member + '\'' +
                ", type=" + type +
                ", dateOfTransaction=" + dateOfTransaction +
                ", fine=" + fine +
                '}';
    }
}
