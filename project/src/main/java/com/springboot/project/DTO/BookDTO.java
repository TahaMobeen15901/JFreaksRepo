package com.springboot.project.DTO;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class BookDTO {

    @NotNull
    private int isbn;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z]+( [a-zA-Z]+)*$")
    private String title;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z]+( [a-zA-Z]+)*$")
    private String author;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z]+( [a-zA-Z]+)*$")
    private String publisher;

    private int edition;

    public BookDTO(int isbn ,String title, String author, String publisher, int edition) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.edition = edition;
    }

    public BookDTO() {
        this(-1,"","","",0);
    }

    public int getIsbn() {
        return isbn;
    }

    public void setIsbn(int isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getEdition() {
        return edition;
    }

    public void setEdition(int edition) {
        this.edition = edition;
    }

    @Override
    public String toString() {
        return "BookDTO{" +
                "isbn=" + isbn +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publisher='" + publisher + '\'' +
                ", edition=" + edition +
                '}';
    }
}
