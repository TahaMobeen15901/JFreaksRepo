package com.springboot.project.service;

import com.springboot.project.DTO.FineDTO;
import com.springboot.project.entity.Book;
import com.springboot.project.entity.Transaction;
import com.springboot.project.entity.TransactionType;

import java.util.List;
import java.util.Map;

public interface TransactionService {

    void borrow(Book book);

    void expire(Transaction transaction);

    void renew(Book book, String userName);

    void returnBook(Book book, String userName);

    void save(Transaction transaction);

    Transaction findById(long id);
    List<Transaction> findByMember();

    List<Transaction> findAll();

    List<Transaction> findByMemberUserNameBookISBNAndTypesIn(String userName, int isbn, List<TransactionType> transTypes);
    List<FineDTO> findFinesForGivenTransactions(List<Transaction> transactions);

    List<Transaction> findByMemberAndTypeIn(String userName);

    List<Integer> findBorrowedByMember();

    Map<Transaction, Double> calcFineForMember();
}
