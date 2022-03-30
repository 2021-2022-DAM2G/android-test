package com.example.myapplication;

import org.iesfm.library.Book;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Este interfaz lo tenemos que hacer simétrico al que tenemos al otro lado, en el backend.
 *
 * El único cambio es que el return type SIEMPRE debe estar entre Call<>
 *
 */
public interface BookClient {


    @POST("/books")
    Call<Void> createBook(@Body Book book);

    @GET("/books")
    Call<List<Book>> getAllBooks();

    @GET("/books")
    Call<List<Book>> getAvailableBooks(@Query("available") boolean available);

    @GET("/books")
    Call<List<Book>> getYearBooks(@Query("year") int year);

    @GET("/authors/{author}/books")
    Call<List<Book>> getBooksByAuthor(@Path("author") String author);

    @PUT("/books/{isbn}")
    Call<Void> updateBook(@Path("isbn") String isbn, @Body Book book);
}






















