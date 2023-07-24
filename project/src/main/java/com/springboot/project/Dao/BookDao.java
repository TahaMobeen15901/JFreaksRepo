package com.springboot.project.Dao;

import com.springboot.project.entity.Book;

import java.util.List;

public interface BookDao {
    void save(Book book);

    List<Book> SearchByTitle(String title);

    List<Book> findAllAvailable();

    List<Book> findAll();

    Book SearchByISBN(int isbn);

    Book findByISBN(int isbn);

    List<Book> SearchByAuthor(String author);

    List<Book> SearchByPublisher(String publisher);

    void delete(int isbn);

}
