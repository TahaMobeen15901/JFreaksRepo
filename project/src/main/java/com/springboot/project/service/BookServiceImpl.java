package com.springboot.project.service;


import com.springboot.project.Dao.BookDao;
import com.springboot.project.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookServiceImpl implements BookService{

    BookDao bookDao;

    @Autowired
    public  BookServiceImpl(BookDao bookDao){
        this.bookDao = bookDao;
    }
    @Override
    public List<Book> findAllAvailable() {
        return bookDao.findAllAvailable();
    }

    @Override
    public List<Book> findAll() {
        return bookDao.findAll();
    }

    @Override
    public void save(Book book) {
        bookDao.save(book);
    }

    @Override
    public Map<String, List<Book>> Search(String query) {
        Map<String, List<Book>> hashMap = new HashMap<>();
        hashMap.put("author", bookDao.SearchByAuthor(query));
        hashMap.put("title", bookDao.SearchByTitle(query));
        hashMap.put("publisher", bookDao.SearchByPublisher(query));
        return hashMap;
    }

    @Override
    public Book Search(int isbn) {
        return bookDao.SearchByISBN(isbn);
    }

    @Override
    public Book find(int isbn) {
        return bookDao.findByISBN(isbn);
    }

    @Override
    public void delete(int isbn) {
        bookDao.delete(isbn);
    }

    @Override
    public boolean returnBook(int isbn) {
        var book = bookDao.findByISBN(isbn);
        if(!book.getStatus().equals("b")){
            return false;
        }
        book.setStatus("a");
        bookDao.save(book);
        return true;
    }

    @Override
    public boolean borrow(int isbn) {
        var book = bookDao.findByISBN(isbn);
        book.setStatus("b");
        bookDao.save(book);
        return true;
    }

    @Override
    public boolean borrowFirstTime(int isbn) {
        var book = bookDao.findByISBN(isbn);
        if (!book.getStatus().equals("a")) return false;
        book.setStatus("b");
        bookDao.save(book);
        return true;
    }
}
