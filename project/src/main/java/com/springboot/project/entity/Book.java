package com.springboot.project.entity;

import jakarta.persistence.*;

@Entity
@Table(name="book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="isbn")
    private int isbn;

    @Column(name="title")
    private String title;

    @Column(name="author")
    private String author;

    @Column(name="publisher")
    private String publisher;

    @Column(name="edition")
    private int edition;

    @Enumerated(EnumType.STRING)
    private BookStatus status;

    public Book(String title, String author, String publisher, int edition) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.edition = edition;
        status = BookStatus.AVAILABLE;
    }

    public Book() {
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

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus bookStatus) {
        this.status = bookStatus;
    }

    @Override
    public String toString() {
        return "Book{" +
                "isbn=" + isbn +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publisher='" + publisher + '\'' +
                ", edition=" + edition +
                ", status=" + status +
                '}';
    }
}
