package com.springboot.project.controller;


import com.springboot.project.DTO.BookDTO;
import com.springboot.project.entity.Book;
import com.springboot.project.entity.BookStatus;

import com.springboot.project.service.BookService;
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

    private final BookService bookService;

    private final TransactionService transactionService;
    @Autowired
    public AdminController(BookService bookService, TransactionService transactionService) {
        this.bookService = bookService;
        this.transactionService = transactionService;
    }


    @GetMapping("/home")
    public String home(@RequestParam(value = "errorMessage", required = false) String error, Model model){
        model.addAttribute("ErrorMessage", error);
        model.addAttribute("Books", bookService.findAllBooks());
        model.addAttribute("Transactions", transactionService.findFinesForGivenTransactions(transactionService.findAll()));
        return "admin-home";
    }

    @GetMapping("/show-new-book-form")
    public String createBookPage(Model model){
        model.addAttribute("Book", new BookDTO());
        return "book-form";
    }
    @PostMapping("/book")
    public String saveAndUpdateBook(@Valid @ModelAttribute("Book") BookDTO bookDTO, BindingResult result, Model model){

        if(result.hasErrors()){
            model.addAttribute("Book", bookDTO);
            return "book-form";
        }
        Book book;

        if(bookDTO.getIsbn() > -1){
            book = bookService.SearchByUserQuery(bookDTO.getIsbn());
            if(( book == null) || book.getStatus()== BookStatus.REMOVED) return "redirect:/admin/home?errorMessage=Error: Invalid ISBN. ";
            book.setAuthor(bookDTO.getAuthor());
            book.setEdition(bookDTO.getEdition());
            book.setTitle(bookDTO.getTitle());
            book.setPublisher(bookDTO.getPublisher());
        } else {
            book = new Book(bookDTO.getTitle(), bookDTO.getAuthor(), bookDTO.getPublisher(), bookDTO.getEdition());
        }
        try {
            bookService.save(book);
        } catch (Exception e){
            return "redirect:/admin/home?errorMessage=Database Error: Book can't be saved at this time";
        }
        return "redirect:/admin/home?success=true";
    }

    @GetMapping("/show-update-book-form")
    public String updateBookPage(@RequestParam("bookISBN") String isbn, Model model){
        try{
            var book = bookService.SearchByUserQuery(Integer.parseInt(isbn));
            var bookDTO = new BookDTO(book.getIsbn(), book.getTitle(), book.getAuthor(), book.getPublisher(), book.getEdition());
            model.addAttribute("Book", bookDTO);
        }catch (NullPointerException e){
            return "redirect:/admin/home?errorMessage=Error: Invalid ISBN. ";
        }
        return "book-form";
    }

    @GetMapping("/delete")
    public String deleteBook(@RequestParam("bookISBN") int isbn){
        var book = bookService.SearchByUserQuery(isbn);
        if(( book == null) || book.getStatus()== BookStatus.REMOVED) return "redirect:/admin/home?errorMessage=Error: Invalid ISBN. ";
        StringBuilder errorMessages = new StringBuilder();
        try{
            bookService.delete(isbn);
            return "redirect:/admin/home?success=true";
        } catch(Exception e){
            return "redirect:/admin/home?errorMessage=DatabaseError: The book couldn't be deleted. ";
        }
    }
}
