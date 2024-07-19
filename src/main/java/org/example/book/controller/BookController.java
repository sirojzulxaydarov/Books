package org.example.book.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.book.dto.BookReq;
import org.example.book.entity.Book;
import org.example.book.repo.BookRepo;
import org.example.book.service.ExcelExportService;
import org.example.book.service.ExcelImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RequestMapping("/books")
@Controller
public class BookController {

    private final BookRepo bookRepo;
    private final ExcelExportService excelExportService;
    private final ExcelImportService excelImportService;

    @GetMapping()
    public String book(Model model) {
        List<Book> books = bookRepo.findAll();
        model.addAttribute("books", books);
        return "books";
    }

    @GetMapping("/create")
    public String create() {
        return "create";
    }

    @GetMapping("/update")
    public String studentUpdatePage(@RequestParam Integer id, Model model) {
        Optional<Book> bookOptional = bookRepo.findById(id);
        Book book = bookOptional.get();
        model.addAttribute("book", book);
        return "create";
    }

    @PostMapping("/update/{id}")
    public String updateStudent(@ModelAttribute BookReq bookReq, @PathVariable Integer id) {
        Book book = Book.builder()
                .title(bookReq.title())
                .author(bookReq.author())
                .price(bookReq.price())
                .id(id)
                .build();
        bookRepo.save(book);
        return "redirect:/books";
    }


    @PostMapping("/delete")
    public String delete(@RequestParam Integer id) {
        bookRepo.deleteById(id);
        return "redirect:/books";

    }

    @PostMapping
    public String save(@ModelAttribute BookReq bookReq) {
        Book book = Book.builder()
                .title(bookReq.title())
                .author(bookReq.author())
                .price(bookReq.price())
                .build();
        bookRepo.save(book);
        return "redirect:/books";
    }

    @GetMapping("/export")
    public void exportExcel(HttpServletResponse response) throws IOException {
        List<Book> books = bookRepo.findAll();
        excelExportService.exportToExcel(response,books);
    }

    @PostMapping("/import")
    public String importBooksFromExcel(MultipartFile file) throws IOException {
        List<Book> books = excelImportService.importBooksFromExcel(file.getInputStream());
        bookRepo.saveAll(books);
        return "redirect:/books";
    }


}
