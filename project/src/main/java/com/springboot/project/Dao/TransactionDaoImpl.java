package com.springboot.project.Dao;


import com.springboot.project.DTO.FineDTO;
import com.springboot.project.entity.Book;
import com.springboot.project.entity.Member;
import com.springboot.project.entity.Transaction;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TransactionDaoImpl implements TransactionDao{

    EntityManager entityManager;

    @Autowired
    public TransactionDaoImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }
    @Override
    @Transactional
    public boolean borrow(String username, int id ) {
        Member member = entityManager.find(Member.class, username);
        Book book = entityManager.find(Book.class, id);
        entityManager.persist(new Transaction(
                book,
                member,
                'b'
        ));
        return true;
    }

    @Override
    @Transactional
    public boolean expire(Transaction transaction) {
        if(transaction == null) return false;
        transaction.setType('e');
        entityManager.merge(transaction);
        return true;
    }

    @Override
    @Transactional
    public void renew(int isbn) {
        var tq = entityManager.createQuery("FROM Transaction WHERE book.isbn = :id AND (type = 'b' OR type = 'n')");
        tq.setParameter("id",isbn);
        List<Transaction> validTransactions = tq.getResultList();
        var member = validTransactions.get(0).getMember();
        var book = entityManager.find(Book.class, isbn);
        for(var transaction: validTransactions){
            transaction.setType('e');
            entityManager.merge(transaction);
        }
        entityManager.persist(new Transaction(book, member, 'n'));
    }

    @Override
    @Transactional
    public void returnBook(int isbn) {
        var tq = entityManager.createQuery("FROM Transaction WHERE book.isbn = :id AND (type = 'b' OR type = 'n')");
        tq.setParameter("id",isbn);
        List<Transaction> validTransactions = tq.getResultList();
        var member = validTransactions.get(0).getMember();
        var book = entityManager.find(Book.class, isbn);
        for(var transaction: validTransactions){
            transaction.setType('e');
            entityManager.merge(transaction);
        }
        entityManager.persist(new Transaction(book, member, 'r'));

    }

    @Override
    public Transaction findById(int id) {
        return entityManager.find(Transaction.class, id);
    }

    @Override
    public List<Transaction> findByMember(String username) {
        var tq = entityManager.createQuery("FROM Transaction WHERE member.userName = :person AND (type = 'b' OR type = 'n')");
        tq.setParameter("person", username);
        return tq.getResultList();
    }

    @Override
    public List<Transaction> findByMemberBook(String username, int id ) {
        var tq = entityManager.createQuery("FROM Transaction WHERE member.userName = :person AND book.isbn = :thing AND type != 'e'");
        tq.setParameter("person", username);
        tq.setParameter("thing", id);
        return tq.getResultList();
    }

    @Override
    public List<Transaction> findAll() {
        return entityManager.createQuery("FROM Transaction").getResultList();
    }

    @Override
    public Map<Transaction, Double> calcFine(String username) {
        var overDueTransactions = new HashMap<Transaction, Double>();
        var tq = entityManager.createQuery("FROM Transaction WHERE member.userName = :person AND (type = 'b' OR type = 'n')");
        tq.setParameter("person", username);
        for(Transaction transaction : (List<Transaction>) tq.getResultList()){
            Period period = Period.between(transaction.getDateOfTransaction(), LocalDate.now());
            double fine = (period.toTotalMonths() * 1000);
            overDueTransactions.put(transaction, fine);
        }
        return overDueTransactions;
    }

    @Override
    public List<Integer> findBorrowedByMember(String userName) {
        var tq = entityManager.createQuery("SELECT book.isbn FROM Transaction WHERE member.userName = :data AND (type = 'b' OR type = 'n')");
        tq.setParameter("data", userName);
        return tq.getResultList();
    }


    @Override
    public List<FineDTO> findAllWithFines(List<Transaction> transactions) {
        List<FineDTO> transWithFines = new ArrayList<>();
        Period period;
        for(Transaction transaction: transactions){
            period = Period.between(transaction.getDateOfTransaction(), LocalDate.now());
            double fine = transaction.getType()=='b' || transaction.getType() == 'n' ? (period.toTotalMonths() * 1000) : -1;
            transWithFines.add(new FineDTO(transaction.getId(), transaction.getBook().getIsbn(),
                    transaction.getMember().getUserName(), transaction.getType(), transaction.getDateOfTransaction(), fine));
        }
        return transWithFines;
    }
}
