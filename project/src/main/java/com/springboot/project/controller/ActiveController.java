package com.springboot.project.controller;


import com.springboot.project.DTO.MemberDTO;
import com.springboot.project.entity.Book;
import com.springboot.project.entity.Member;
import com.springboot.project.entity.Transaction;
import com.springboot.project.service.BookService;
import com.springboot.project.service.MemberService;
import com.springboot.project.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RequestMapping("/active")
@Controller
public class ActiveController {

    PasswordEncoder passwordEncoder;
    MemberService memberService;
    BookService bookService;

    TransactionService transactionService;
    @Autowired
    public ActiveController(MemberService memberService, BookService bookService, TransactionService transactionService, PasswordEncoder passwordEncoder) {
        this.memberService = memberService;
        this.bookService = bookService;
        this.transactionService = transactionService;
        this.passwordEncoder = passwordEncoder;
    }


    @GetMapping("/home")
    public String Home(){
       return "active-home";
    }

    @GetMapping("/books")
    public String books(Model model){
        model.addAttribute("Books", bookService.findAllAvailable());
        return "books";
    }
    @PostMapping("/search")
    public String search(@RequestParam("theSearch") String query, Model model){
        if(query.matches("^[0-9]*$")){
            model.addAttribute("BooksLists", new HashMap<String, List<Book>>().put("ISBN", List.of(bookService.Search(Integer.parseInt(query)))));
        } else {
            model.addAttribute("BooksLists", bookService.Search(query));
        }
        return "search";
    }

    @GetMapping("/borrowed-books")
    public String borrowed(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        model.addAttribute("Books", transactionService.findByMember(userName).stream()
                .map(Transaction::getBook)
                .toList());

        return "borrowed-books";
    }

    @GetMapping("/update-member-page")
    public String updateInfo(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberService.findUserByUserName(authentication.getName());
        MemberDTO memberDTO = new MemberDTO(member.getUserName(), member.getName(), member.getPassword(), member.getEnabled(), member.getDob(), member.getPhone());
        model.addAttribute("Member", memberDTO);
        return "update-member";
    }

    @PostMapping("/update")
    public String updateAction(@ModelAttribute("Member") MemberDTO memberDTO) {


        Member existingMember = memberService.findUserByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
        memberDTO.setUserName(SecurityContextHolder.getContext().getAuthentication().getName());
        if (memberDTO.getPassword() == null || memberDTO.getPassword().trim().isEmpty()) {
            memberDTO.setPassword(existingMember.getPassword());
        } else {
            String encodedPassword = passwordEncoder.encode(memberDTO.getPassword());
            memberDTO.setPassword(encodedPassword);
        }

        memberService.updateMember(memberDTO);

        return "redirect:/active/home?success";
    }


    @PostMapping("/delete")
    public String deleteMember(@ModelAttribute("Member") MemberDTO memberDTO){
        Member member = memberService.findUserByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
        memberService.deleteMember(member);
        return "redirect:/show-login-page";
    }

    @GetMapping("/fines")
    public String fines(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Map<Transaction, Double> fines = transactionService.calcFine(authentication.getName());
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
        int transID = Integer.parseInt(id);
        var transaction = transactionService.findById(transID);
        if(!transaction.getMember().getUserName().equals(SecurityContextHolder.getContext().getAuthentication().getName())){
            return "active-home";
        }
        var isbn = transaction.getBook().getIsbn();
        bookService.returnBook(isbn);
        transactionService.returnBook(isbn);
        return "redirect:/active/fines";
    }

    @PostMapping("/return")
    public String returnBook(@RequestParam("bookISBN") String isbn){
        List<Integer> isbns = transactionService.findBorrowedByMember(SecurityContextHolder.getContext().getAuthentication().getName());
        int intISBN = Integer.parseInt(isbn);
        for(int ISBN : isbns){
            if (intISBN == ISBN){
                bookService.returnBook(intISBN);
                transactionService.returnBook(intISBN);
                return "redirect:/active/borrowed-books";
            }
        }
        return "active-home";
    }
    @PostMapping("/renew")
    public String renewBook(@RequestParam("bookISBN") String isbn){
        List<Integer> isbns = transactionService.findBorrowedByMember(SecurityContextHolder.getContext().getAuthentication().getName());
        int intISBN = Integer.parseInt(isbn);
        for(int ISBN : isbns){
            if (intISBN == ISBN){
                bookService.borrow(intISBN);
                transactionService.renew(intISBN);

                return "redirect:/active/borrowed-books";
            }
        }
        return "active-home";
    }
    @PostMapping("/borrow")
    public String borrowBook(@RequestParam("bookISBN") String isbn){
        int intISBN = Integer.parseInt(isbn);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(bookService.borrowFirstTime(intISBN)){
            transactionService.borrow(authentication.getName(), intISBN);
            return "redirect:/active/borrowed-books";
        }
        return "active-home";
    }
}
