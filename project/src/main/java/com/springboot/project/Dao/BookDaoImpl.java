package com.springboot.project.Dao;

import com.springboot.project.entity.Book;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class BookDaoImpl implements BookDao{

    EntityManager entityManager;

    @Autowired
    public BookDaoImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }
    @Override
    @Transactional
    public void save(Book book) {
        var temp = entityManager.find(Book.class, book.getIsbn());
        if(temp == null){
            entityManager.persist(book);
        }
        else {
            entityManager.merge(book);
        }
    }

    @Override
    public List<Book> findAllAvailable() {
        var tq = entityManager.createQuery("FROM Book WHERE status = 'a'");
        return tq.getResultList();
    }

    @Override
    public List<Book> findAll() {
        List<Book> result = entityManager.createQuery("FROM Book WHERE status != 'r'").getResultList();
        return result;
    }

    @Override
    public List<Book> SearchByTitle(String title) {
        var tq = entityManager.createQuery("FROM Book WHERE title LIKE :data AND status = 'a'");
        tq.setParameter("data", "%" + title + "%");
        return tq.getResultList();
    }

    @Override
    public Book SearchByISBN(int isbn) {
        var tq = entityManager.createQuery("FROM Book WHERE isbn = :data AND status = 'a'");
        tq.setParameter("data", isbn);
        return (Book) tq.getSingleResult();
    }

    @Override
    public Book findByISBN(int isbn) {
        var tq = entityManager.createQuery("FROM Book WHERE isbn = :data");
        tq.setParameter("data", isbn);
        return (Book) tq.getSingleResult();
    }

    @Override
    public List<Book> SearchByAuthor(String author) {
        var tq = entityManager.createQuery("FROM Book WHERE author LIKE :data AND status = 'a'");
        tq.setParameter("data", "%" + author + "%");
        return tq.getResultList();
    }

    @Override
    public List<Book> SearchByPublisher(String publisher) {
        var tq = entityManager.createQuery("FROM Book WHERE publisher LIKE :data AND status = 'a'");
        tq.setParameter("data", "%" + publisher + "%");
        return tq.getResultList();
    }

    @Override
    @Transactional
    public void delete(int isbn) {
        var book = entityManager.find(Book.class, isbn);
        book.setStatus("r");
        entityManager.merge(book);
    }
}
