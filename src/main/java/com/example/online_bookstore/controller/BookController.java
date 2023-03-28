package com.example.online_bookstore.controller;

import com.example.online_bookstore.dto.request.BookDTO;
import com.example.online_bookstore.dto.response.BookResponse;
import com.example.online_bookstore.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/books")
public class BookController {

    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<BookResponse>> getBooks() {
        return ResponseEntity.ok(this.service.getBooks());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<BookResponse> getBook(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(this.service.getBook(id));
    }

    @PostMapping
    public ResponseEntity<Void> addBook(@Valid @RequestBody BookDTO bookDTO) {
        this.service.addNewBook(bookDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @PostMapping(path = "/multiple")
    public ResponseEntity<Void> addBooks(@Valid @RequestBody List<BookDTO> bookDTOS) {
        bookDTOS.forEach(this.service::addNewBook);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Void> updateBook(@PathVariable(name = "id") Long id, @Valid @RequestBody BookDTO bookDTO) {
        this.service.updateBook(bookDTO, id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable(name = "id") Long id) {
        this.service.deleteBook(id);
        return ResponseEntity.ok().build();
    }
}
