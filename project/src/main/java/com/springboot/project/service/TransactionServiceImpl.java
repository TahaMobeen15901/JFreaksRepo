package com.springboot.project.service;

import com.springboot.project.DTO.FineDTO;
import com.springboot.project.Dao.TransactionDao;
import com.springboot.project.entity.Book;
import com.springboot.project.entity.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TransactionServiceImpl implements TransactionService{

    TransactionDao transactionDao;

    @Autowired
    public  TransactionServiceImpl(TransactionDao transactionDao){
        this.transactionDao = transactionDao;
    }

    @Override
    public boolean borrow(String username, int id) {
        return transactionDao.borrow(username, id);
    }

    @Override
    public boolean expire(Transaction transaction) {
        return transactionDao.expire(transaction);
    }

    @Override
    public void renew(int isbn) {
        transactionDao.renew(isbn);
    }

    @Override
    public void returnBook(int isbn) {
        transactionDao.returnBook(isbn);
    }

    @Override
    public Transaction findById(int id) {
        return transactionDao.findById(id);
    }

    @Override
    public List<Transaction> findByMemberBook(String username, int id) {
        return transactionDao.findByMemberBook(username, id);
    }

    @Override
    public List<Transaction> findByMember(String username) {
        return transactionDao.findByMember(username);
    }

    @Override
    public List<Transaction> findAll() {
        return transactionDao.findAll();
    }

    @Override
    public Map<Transaction, Double> calcFine(String username) {
        return transactionDao.calcFine(username);
    }

    @Override
    public List<Integer> findBorrowedByMember(String userName) {
        return transactionDao.findBorrowedByMember(userName);
    }

    @Override
    public List<FineDTO> findAllWithFines(List<Transaction> transactions) {
        return transactionDao.findAllWithFines(transactions);
    }
}
