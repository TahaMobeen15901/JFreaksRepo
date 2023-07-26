package com.springboot.project.service;

import com.springboot.project.entity.Book;

import java.util.List;
import java.util.Map;

public interface BookService {
    void save(Book book);

    Map<String,List<Book>> SearchByUserQuery(String query);

    List<Book> findAllAvailableBooks();

    List<Book> findAllBooks();

    Book SearchByUserQuery(int isbn);

    Book find(int isbn);

    void delete(int isbn);

    void returnBook(Book book);

    void borrow(Book book);

}
