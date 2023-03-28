package com.example.online_bookstore.controller;


import com.example.online_bookstore.SharedTestClass;
import com.example.online_bookstore.dto.response.BookResponse;
import com.example.online_bookstore.entity.Book;
import com.example.online_bookstore.exception.ResourceNotFoundException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.stream.StreamSupport;

import static java.lang.String.valueOf;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
class BookControllerTest extends SharedTestClass {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    void addBooksToDatabase() throws Exception {
        var booksRequest = getBooksRequest();
        mockMvc.perform(post("/books/multiple")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(booksRequest)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        var books = StreamSupport.stream(bookRepository.findAll().spliterator(), false).toList();

        assertEquals(8, books.size());
        var bookTitles = books.stream().map(Book::getTitle).toList();
        List.of(
                "The Last Wish",
                "Sword of Destiny",
                "Blood of Elves",
                "Time of Contempt",
                "Baptism of Fire",
                "The Tower of The Swallow",
                "The Lady of the Lake",
                "Season of Storms").forEach(title -> {
            assertTrue(bookTitles.contains(title));
        });
    }

    @Test
    @Order(2)
    void addBookToDatabase() throws Exception {
        var bookRequest = getBookRequest();
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookRequest)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        var books = StreamSupport.stream(bookRepository.findAll().spliterator(), false).toList();

        assertEquals(9, books.size());
        assertTrue(books.stream().map(Book::getTitle).toList().contains("Hobbit"));

    }

    @Test
    @Order(3)
    void updateExistingBook() throws Exception {
        var book = bookRepository.findById(9L).orElseThrow(() -> new ResourceNotFoundException("No book in database"));
        assertEquals("Hobbit", book.getTitle());
        assertEquals(new BigDecimal(valueOf(8.5)), book.getPrice().stripTrailingZeros());

        var updatedPriceRequest = getBookUpdatedPriceRequest();
        mockMvc.perform(put("/books/9")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedPriceRequest)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        book = bookRepository.findById(9L).orElseThrow(() -> new ResourceNotFoundException("No book in database"));

        assertEquals("Hobbit", book.getTitle());
        assertEquals(new BigDecimal(valueOf(15)), book.getPrice().stripTrailingZeros());

    }

    @Test
    @Order(4)
    void getBooks() throws Exception {
        var result = mockMvc.perform(get("/books").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        var response = objectMapper
                .readValue(result.getResponse().getContentAsString(), new TypeReference<List<BookResponse>>() {
                });

        assertFalse(response.isEmpty());
        assertEquals(9, response.size());
    }

    private String getBookUpdatedPriceRequest() {
        return """
                {
                    "title": "Hobbit",
                    "author": "J. R. R. Tolkien",
                    "type": "OLD",
                    "price": "15.00"
                }
                """;
    }


    private String getBookRequest() {
        return """
                {
                    "title": "Hobbit",
                    "author": "J. R. R. Tolkien",
                    "type": "OLD",
                    "price": 8.50
                }
                """;
    }

    private String getBooksRequest() {
        return """
                [
                    {
                        "title": "The Last Wish",
                        "author": "Andrzej Sapkowski",
                        "type": "OLD",
                        "price": 8.50
                    },
                    {
                        "title": "Sword of Destiny",
                        "author": "Andrzej Sapkowski",
                        "type": "OLD",
                        "price": 8.50
                    },
                    {
                        "title": "Blood of Elves",
                        "author": "Andrzej Sapkowski",
                        "type": "OLD",
                        "price": 8.50
                    },
                    {
                        "title": "Time of Contempt",
                        "author": "Andrzej Sapkowski",
                        "type": "REGULAR",
                        "price": 8.50
                    },
                    {
                        "title": "Baptism of Fire",
                        "author": "Andrzej Sapkowski",
                        "type": "REGULAR",
                        "price": 8.50
                    },
                    {
                        "title": "The Tower of The Swallow",
                        "author": "Andrzej Sapkowski",
                        "type": "REGULAR",
                        "price": 8.50
                    },{
                        "title": "The Lady of the Lake",
                        "author": "Andrzej Sapkowski",
                        "type": "NEW",
                        "price": 8.50
                    },{
                        "title": "Season of Storms",
                        "author": "Andrzej Sapkowski",
                        "type": "NEW",
                        "price": 8.50
                    }
                ]
                """;
    }

}
