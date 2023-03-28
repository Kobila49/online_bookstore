package com.example.online_bookstore.service.implementation;

import com.example.online_bookstore.dto.request.BookDTO;
import com.example.online_bookstore.dto.response.BookResponse;
import com.example.online_bookstore.entity.Book;
import com.example.online_bookstore.exception.ResourceNotFoundException;
import com.example.online_bookstore.repository.BookRepository;
import com.example.online_bookstore.service.BookService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class BookServiceImplementation implements BookService {

    private final BookRepository repository;

    public BookServiceImplementation(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<BookResponse> getBooks() {
        return StreamSupport
                .stream(repository.findAll().spliterator(), false)
                .map(BookServiceImplementation::bookResponse)
                .toList();
    }

    @Override
    public void addNewBook(BookDTO bookDTO) {
        repository.save(createBook(bookDTO));
    }

    @Override
    public void updateBook(BookDTO bookDTO, Long id) {
        var book = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found book with given id: %d".formatted(id)));
        repository.save(updateExistingBook(book, bookDTO));
    }

    @Override
    public void deleteBook(Long id) {
        var book = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found book with given id: %d".formatted(id)));
        this.repository.delete(book);

    }

    @Override
    public BookResponse getBook(Long id) {
        return repository.findById(id)
                .map(BookServiceImplementation::bookResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Not found book with given id: %d".formatted(id)));
    }

    private static BookResponse bookResponse(Book book) {
        return new BookResponse(
                book.getId(), book.getTitle(), book.getAuthor(), book.getType(), book.getPrice());
    }


    private Book updateExistingBook(Book book, BookDTO bookDTO) {
        return book.toBuilder()
                .title(bookDTO.title())
                .author(bookDTO.author())
                .type(bookDTO.type())
                .price(bookDTO.price())
                .build();
    }

    private Book createBook(BookDTO bookDTO) {
        return Book.builder()
                .title(bookDTO.title())
                .author(bookDTO.author())
                .type(bookDTO.type())
                .price(bookDTO.price())
                .build();
    }
}
