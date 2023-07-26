package com.springboot.project.Dao;

import com.springboot.project.entity.Book;
import com.springboot.project.entity.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {

    Book findByIsbn(int isbn);

    List<Book> findByStatus(BookStatus bookStatus);

    List<Book> findByTitleContainingAndStatus(String title, BookStatus bookStatus);
    List<Book> findByAuthorContainingAndStatus(String author, BookStatus bookStatus);
    List<Book> findByPublisherContainingAndStatus(String publisher, BookStatus bookStatus);
}
