package com.springboot.project.service;


import com.springboot.project.Dao.BookRepository;
import com.springboot.project.entity.Book;
import com.springboot.project.entity.BookStatus;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookServiceImpl implements BookService{

    private final BookRepository bookRepository;

    @Autowired
    public  BookServiceImpl(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }
    @Override
    public List<Book> findAllAvailableBooks() {
        return bookRepository.findByStatus(BookStatus.AVAILABLE);
    }

    @Override
    public List<Book> findAllBooks() {
         return bookRepository.findAll();
    }

    @Override
    @Transactional
    public void save(Book book){
        bookRepository.save(book);
    }

    @Override
    public Map<String, List<Book>> SearchByUserQuery(String query) {
        Map<String, List<Book>> hashMap = new HashMap<>();
        hashMap.put("author", bookRepository.findByAuthorContainingAndStatus(query, BookStatus.AVAILABLE));
        hashMap.put("publisher", bookRepository.findByPublisherContainingAndStatus(query, BookStatus.AVAILABLE));
        hashMap.put("title", bookRepository.findByTitleContainingAndStatus(query, BookStatus.AVAILABLE));
        return hashMap;
    }

    @Override
    public Book SearchByUserQuery(int isbn){
        var book = this.find(isbn);
        if (book == null) return null;
        if(book.getStatus() == BookStatus.AVAILABLE){
            return book;
        } else {
            return null;
        }

    }

    @Override
    public Book find(int isbn) {
       return bookRepository.findByIsbn(isbn);
    }

    @Override
    @Transactional
    public void delete(int isbn){
        var book = this.find(isbn);
        book.setStatus(BookStatus.REMOVED);
        bookRepository.save(book);
    }

    @Override
    @Transactional
    public void returnBook(Book book){
        book.setStatus(BookStatus.AVAILABLE);
        this.save(book);
    }

    @Override
    @Transactional
    public void borrow(Book book){
        book.setStatus(BookStatus.BORROWED);
        this.save(book);
    }

}
