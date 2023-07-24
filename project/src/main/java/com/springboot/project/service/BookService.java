package com.springboot.project.service;

import com.springboot.project.entity.Book;

import java.util.List;
import java.util.Map;

public interface BookService {
    void save(Book book);

    Map<String,List<Book>> Search(String query);

    List<Book> findAllAvailable();

    List<Book> findAll();

    Book Search(int isbn);

    Book find(int isbn);

    void delete(int isbn);

    boolean returnBook(int isbn);

    boolean borrow(int isbn);

    boolean borrowFirstTime(int isbn);
}
