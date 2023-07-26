package com.springboot.project.service;
import com.springboot.project.DTO.FineDTO;
import com.springboot.project.Dao.TransactionRepository;
import com.springboot.project.entity.Book;
import com.springboot.project.entity.Transaction;
import com.springboot.project.entity.TransactionType;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;

@Service
public class TransactionServiceImpl implements TransactionService{


    private final MemberService memberService;

    private final BookService bookService;

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, MemberService memberService, BookService bookService){
        this.transactionRepository = transactionRepository;
        this.memberService = memberService;
        this.bookService = bookService;
    }

    @Override
    public List<Transaction> findByMemberUserNameBookISBNAndTypesIn(String userName, int isbn, List<TransactionType> transTypes) {
        return transactionRepository.findByMemberUserNameAndBookIsbnAndTypeIn(userName, isbn, transTypes);
    }

    @Override
    @Transactional
    public void borrow(Book book){
        var member = memberService.findByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
        bookService.borrow(book);
        this.save(new Transaction(book, member, TransactionType.BORROW));
    }

    @Override
    @Transactional
    public void expire(Transaction transaction){
       transaction.setType(TransactionType.EXPIRE);
       this.save(transaction);
    }

    @Override
    @Transactional
    public void save(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    @Override
    @Transactional
    public void renew(Book book, String userName){
        bookService.borrow(book);
        transactionRepository.save(new Transaction(book, memberService.findByUserName(userName), TransactionType.RENEW));
    }

    @Override
    @Transactional
    public void returnBook(Book book, String userName){
        bookService.returnBook(book);
        transactionRepository.save(new Transaction(book, memberService.findByUserName(userName), TransactionType.RETURN));
    }

    @Override
    public Transaction findById(long id) {
        try{
            return transactionRepository.findById(id);
        } catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    @Override
    public List<Transaction> findByMember() {
        String sessionUser = SecurityContextHolder.getContext().getAuthentication().getName();
        try{
            return transactionRepository.findByMemberUserNameAndTypeIn(sessionUser, List.of(TransactionType.RENEW, TransactionType.BORROW));
        } catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    @Override
    public List<Transaction> findByMemberAndTypeIn(String userName) {
        return transactionRepository.findByMemberUserNameAndTypeIn(userName, List.of(TransactionType.RENEW, TransactionType.BORROW));
    }

    @Override
    public List<Transaction> findAll() {
        try{
            return transactionRepository.findAll();
        } catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    @Override
    public Map<Transaction, Double> calcFineForMember() {
        String sessionUser = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Transaction> transactions;
        transactions = transactionRepository.findByMemberUserNameAndTypeIn(sessionUser, List.of(TransactionType.RENEW, TransactionType.BORROW));
        var overDueTransactions = new HashMap<Transaction, Double>();
        transactions.forEach(transaction -> overDueTransactions.put(transaction, (double) Period.between(transaction.getDateOfTransaction(), LocalDate.now()).toTotalMonths()*1000));
        return overDueTransactions;
    }

    @Override
    public List<Integer> findBorrowedByMember() {
        String sessionUser = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Transaction> transactions;
        try {
            transactions = transactionRepository.findByMemberUserNameAndTypeIn(sessionUser, List.of(TransactionType.RENEW, TransactionType.BORROW));
        }catch (EmptyResultDataAccessException e){
            return null;
        }
        return transactions.stream()
                .map(transaction -> transaction.getBook().getIsbn())
                .toList();
    }

    @Override
    public List<FineDTO> findFinesForGivenTransactions(List<Transaction> transactions) {
        if(transactions == null) return null;
        List<FineDTO> transWithFines = new ArrayList<>();
        transactions.forEach(transaction -> transWithFines.add(new FineDTO(
                transaction.getId(),
                transaction.getBook().getIsbn(),
                transaction.getMember().getUserName(),
                transaction.getType(),
                transaction.getDateOfTransaction(),
                transaction.getType()==TransactionType.BORROW || transaction.getType() == TransactionType.RENEW
                        ? (Period.between(transaction.getDateOfTransaction(), LocalDate.now()).toTotalMonths() * 1000) : -1
        )));
        return transWithFines;
    }
}
