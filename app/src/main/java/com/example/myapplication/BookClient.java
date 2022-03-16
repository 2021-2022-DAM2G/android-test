package com.example.myapplication;

import org.iesfm.library.Book;
import org.iesfm.library.client.BooksApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class BookClient implements BooksApi {

    private RestTemplate restTemplate;
    private String host;

    public BookClient(
            RestTemplate restTemplate,
            @Value("${book.api}") String host) {
        this.restTemplate = restTemplate;
        this.host = host;
    }

    @Override
    public void post(Book book) {
        restTemplate.postForObject(host + "/books", book, Void.class);
    }

    @Override
    public List<Book> getBooks(Boolean available, Integer year) {
        HashMap<String, Object> params = new HashMap<>();
        if (available != null) {
            params.put("available", available);
        }
        if (year != null) {
            params.put("year", year);
        }
        Book[] books = restTemplate.getForObject(host + "/books", Book[].class, params);
        return Arrays.asList(books);
    }

    @Override
    public List<Book> getBooksByAuthor(String author) {
        try {
            Book[] books = restTemplate.getForObject(URLEncoder.encode(host + "/author/" + author, StandardCharsets.UTF_8.name()), Book[].class);

            return Arrays.asList(books);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    @Override
    public void updateBook(String isbn, Book book) {
        restTemplate.put(host + "/books/" + isbn, book, Void.class);
    }
}






















