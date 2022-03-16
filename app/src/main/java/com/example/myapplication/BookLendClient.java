package com.example.myapplication;

import org.iesfm.library.BookLend;
import org.iesfm.library.client.BookLendsApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class BookLendClient implements BookLendsApi {

    private RestTemplate restTemplate;
    private String host;

    public BookLendClient(
            RestTemplate restTemplate,
            @Value("${booklend.api}") String host) {
        this.restTemplate = restTemplate;
        this.host = host;
    }

    @Override
    public List<BookLend> list() {
        BookLend[] booklends = restTemplate
                .getForObject(host + "/booklends", BookLend[].class);
        return List.of(booklends);
    }

    @Override
    public void insert(BookLend booklend) {

    }

    @Override
    public List<BookLend> listByNif(String nif) {
        return null;
    }

    @Override
    public void returnBook(int bookLendId) {

    }
}
