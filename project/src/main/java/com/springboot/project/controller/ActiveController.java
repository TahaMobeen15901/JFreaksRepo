package com.springboot.project.controller;


import com.springboot.project.DTO.MemberDTO;
import com.springboot.project.entity.*;
import com.springboot.project.service.BookService;
import com.springboot.project.service.MemberService;
import com.springboot.project.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequestMapping("/active")
@Controller
public class ActiveController {

    private final PasswordEncoder passwordEncoder;
    private final MemberService memberService;
    private final BookService bookService;

    private final TransactionService transactionService;
    @Autowired
    public ActiveController(MemberService memberService, BookService bookService, TransactionService transactionService, PasswordEncoder passwordEncoder) {
        this.memberService = memberService;
        this.bookService = bookService;
        this.transactionService = transactionService;
        this.passwordEncoder = passwordEncoder;
    }


    @GetMapping("/home")
    public String home(@RequestParam(value = "errorMessage", required = false) String error, Model model){
        model.addAttribute("ErrorMessage", error);
       return "active-home";
    }

    @GetMapping("/books")
    public String books(Model model){
        model.addAttribute("Books", bookService.findAllAvailableBooks());
        return "books";
    }
    @PostMapping("/search")
    public String search(@RequestParam("theSearch") String query, Model model){
        if (query.matches("^[0-9]+$")) {
            model.addAttribute("BooksLists", new HashMap<String, List<Book>>().put("ISBN", List.of(bookService.SearchByUserQuery(Integer.parseInt(query)))));
        } else {
            model.addAttribute("BooksLists", bookService.SearchByUserQuery(query));
        }
        return "search";
    }

    @GetMapping("/borrowed-books")
    public String borrowed(@RequestParam(value = "errorMessage", required = false) String error ,Model model){
        model.addAttribute("ErrorMessage", error);
        model.addAttribute("Books", transactionService.findByMember().stream()
                .filter(transaction -> !(transaction.getType() == TransactionType.EXPIRE))
                .map(Transaction::getBook)
                .toList());
        return "borrowed-books";
    }

    @GetMapping("/update-member-page")
    public String updateInfo(@RequestParam(value = "errorMessage", required = false) String error ,Model model){
        model.addAttribute("ErrorMessage", error);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberService.findByUserName(authentication.getName());
        MemberDTO memberDTO = new MemberDTO(member.getUserName(), member.getName(), member.getPassword(), member.getEnabled(), member.getDob(), member.getPhone());
        model.addAttribute("Member", memberDTO);

        return "update-member";
    }

    @PostMapping("/update")
    public String updateAction(@Valid @ModelAttribute("Member") MemberDTO memberDTO, BindingResult result, Model model) {
        if(result.hasErrors()){
            model.addAttribute("Member", memberDTO);
            return "update-member";
        }
        String sessionUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        if(memberDTO.getUserName().equals(sessionUserName)){
            Member existingMember = memberService.findByUserName(sessionUserName);
            if (memberDTO.getPassword() == null || memberDTO.getPassword().trim().isEmpty()) {
                memberDTO.setPassword(existingMember.getPassword());
            } else {
                String encodedPassword = passwordEncoder.encode(memberDTO.getPassword());
                memberDTO.setPassword(encodedPassword);
            }
            memberService.updateMember(memberDTO);
            return "redirect:/active/home?success=true";
        } else{
            return ("redirect:/active/update-member-page?errorMessage=" +"Session Violation Attempted");
        }

    }


    @PostMapping("/delete")
    public String deleteMember(@ModelAttribute("Member") MemberDTO memberDTO){
        Member member = memberService.findByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
        memberService.deleteMember(member);
        return "redirect:/show-login-page?delete=true";
    }

    @GetMapping("/fines")
    public String fines(@RequestParam(value = "errorMessage", required = false) String error,Model model){
        model.addAttribute("ErrorMessage", error);
        Map<Transaction, Double> fines = transactionService.calcFineForMember();
        double totalFine=0;
        for(Double fine : fines.values()){
            totalFine += fine;
        }
        model.addAttribute("Sum_Total", totalFine);
        model.addAttribute("Fines", fines);
        return "fines";
    }

    @PostMapping("/pay-and-return")
    public String payAndReturn(@RequestParam("transactionId") String id){
        String sessionUser = SecurityContextHolder.getContext().getAuthentication().getName();
        var transactions = transactionService.findByMemberAndTypeIn(sessionUser);
        if(transactions.size()==0){
            return "redirect:/active/fines?errorMessage=Error: Access Denied";
        }
        try{
            Transaction temp = transactions.stream()
                    .filter(transaction -> transaction.getId() == Integer.parseInt(id))
                    .toList().get(0);
            transactionService.returnBook(temp.getBook(), sessionUser);
            transactions.forEach(transactionService::expire);
            return "redirect:/active/fines?success=true";
        } catch (IndexOutOfBoundsException e){
            return "redirect:/active/fines?errorMessage=Error: The provided transaction id didn't match anything";
        }

    }

    @PostMapping("/return")
    public String returnBook(@RequestParam("bookISBN") String isbn){
        int intISBN = Integer.parseInt(isbn);
        var book = bookService.find(Integer.parseInt(isbn));
        if(book == null || book.getStatus()== BookStatus.AVAILABLE || book.getStatus()==BookStatus.REMOVED){
            return "redirect:/active/borrowed-books?errorMessage=Error: Book not borrowed by you. ";
        }
        String sessionUser = SecurityContextHolder.getContext().getAuthentication().getName();
        var transactions = transactionService.findByMemberUserNameBookISBNAndTypesIn(sessionUser, intISBN, List.of(TransactionType.RENEW, TransactionType.BORROW));
        if(transactions.size()>0){
            transactions.forEach(transactionService::expire);
            try{
                transactionService.returnBook(book, sessionUser);
                return "redirect:/active/borrowed-books?success=true";
            } catch (Exception e){
                return "redirect:/active/borrowed-books?errorMessage=DatabaseError: Couldn't register the transaction";
            }
        }
        return "redirect:/active/borrowed-books?errorMessage=Error: Book not borrowed by you. ";
    }
    @PostMapping("/renew")
    public String renewBook(@RequestParam("bookISBN") String isbn){
        int intISBN = Integer.parseInt(isbn);
        var book = bookService.find(Integer.parseInt(isbn));
        if(book == null || book.getStatus()== BookStatus.AVAILABLE || book.getStatus()==BookStatus.REMOVED){
            return "redirect:/active/borrowed-books?errorMessage=Error: Book not borrowed by you. ";
        }
        String sessionUser = SecurityContextHolder.getContext().getAuthentication().getName();
        var transactions = transactionService.findByMemberUserNameBookISBNAndTypesIn(sessionUser, intISBN, List.of(TransactionType.RENEW, TransactionType.BORROW));
        if(transactions.size()>0){
            transactions.forEach(transactionService::expire);
            try{
                transactionService.renew(book, sessionUser);
                return "redirect:/active/borrowed-books?success=true";
            } catch (Exception e){
                return "redirect:/active/borrowed-books?errorMessage=DatabaseError: Couldn't register the transaction";
            }
        }
        return "redirect:/active/borrowed-books?errorMessage=Error: Book not borrowed by you. ";
    }
    @PostMapping("/borrow")
    public String borrowBook(@RequestParam("bookISBN") String isbn){
        int intISBN = Integer.parseInt(isbn);
        var book = bookService.SearchByUserQuery(intISBN);
        if(book == null) return "redirect:/active/home?errorMessage=Book Not Available";
        try {
            transactionService.borrow(book);
            return "redirect:/active/borrowed-books?success=true";
        } catch(Exception e){
            return "redirect:/active/home?errorMessage=Error: Couldn't borrow the book. Try again after sometime!";
        }
    }
}
