package com.springboot.project.Dao;

import com.springboot.project.DTO.FineDTO;
import com.springboot.project.entity.Book;
import com.springboot.project.entity.Member;
import com.springboot.project.entity.Transaction;

import java.util.List;
import java.util.Map;

public interface TransactionDao {

    boolean borrow(String username, int id );

    boolean expire(Transaction transaction);

    void renew(int isbn);

    void returnBook(int isbn);

    List<Transaction> findByMemberBook(String username, int id);

    Transaction findById(int id);


    List<Transaction> findByMember(String username);

    List<Transaction> findAll();

    List<FineDTO> findAllWithFines(List<Transaction> transactions);

    List<Integer> findBorrowedByMember(String userName);

    Map<Transaction, Double> calcFine(String username);

}
