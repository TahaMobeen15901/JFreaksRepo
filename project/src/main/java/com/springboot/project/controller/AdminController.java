package com.springboot.project.controller;


import com.springboot.project.DTO.BookDTO;
import com.springboot.project.entity.Book;
import com.springboot.project.service.BookService;
import com.springboot.project.service.MemberService;
import com.springboot.project.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/admin")
@Controller
public class AdminController {

    MemberService memberService;
    BookService bookService;

    TransactionService transactionService;
    @Autowired
    public AdminController(MemberService memberService, BookService bookService, TransactionService transactionService) {
        this.memberService = memberService;
        this.bookService = bookService;
        this.transactionService = transactionService;
    }


    @GetMapping("/home")
    public String Home(Model model){
        model.addAttribute("Books", bookService.findAll());
        model.addAttribute("Transactions", transactionService.findAllWithFines(transactionService.findAll()));
        return "admin-home";
    }

    @GetMapping("/show-new-book-form")
    public String createBookPage(Model model){
        model.addAttribute("Book", new BookDTO());
        return "book-form";
    }
    @PostMapping("/book")
    public String saveBook(@Valid @ModelAttribute("Book") BookDTO bookDTO, BindingResult result, Model model){

        if(result.hasErrors()){
            model.addAttribute("Book", bookDTO);
            return "book-form";
        }
        Book book;
        System.out.println(bookDTO.getIsbn());
        if(bookDTO.getIsbn() > -1){
            book = bookService.Search(bookDTO.getIsbn());
            book.setAuthor(bookDTO.getAuthor());
            book.setEdition(bookDTO.getEdition());
            book.setTitle(bookDTO.getTitle());
            book.setPublisher(bookDTO.getPublisher());
        } else {
            book = new Book(bookDTO.getTitle(), bookDTO.getAuthor(), bookDTO.getPublisher(), bookDTO.getEdition());
            System.out.println(book.getIsbn());
        }
        bookService.save(book);
        return "redirect:/admin/home";
    }

    @GetMapping("/show-update-book-form")
    public String updateBookPage(@RequestParam("bookISBN") String isbn, Model model){
        var book = bookService.Search(Integer.parseInt(isbn));
        var bookDTO = new BookDTO(book.getIsbn() ,book.getTitle(), book.getAuthor(), book.getPublisher(), book.getEdition());
        model.addAttribute("Book", bookDTO);
        return "book-form";
    }

    @GetMapping("/delete")
    public String deleteBook(@RequestParam("bookISBN") int isbn){
        bookService.delete(isbn);
        return "redirect:/admin/home";
    }

}
