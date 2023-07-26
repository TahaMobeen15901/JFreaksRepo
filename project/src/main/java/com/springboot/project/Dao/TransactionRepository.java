package com.springboot.project.Dao;

import com.springboot.project.entity.Transaction;
import com.springboot.project.entity.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByMemberUserNameAndBookIsbnAndTypeIn(String userName, int isbn, List<TransactionType> types);

    List<Transaction> findByMemberUserNameAndTypeIn(String userName, List<TransactionType> types);


    Transaction findById(long id);

}
