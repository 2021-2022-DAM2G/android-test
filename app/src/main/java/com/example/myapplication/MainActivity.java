package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import org.iesfm.library.Book;
import org.iesfm.library.client.BookClient;
import org.iesfm.library.client.BooksApi;
import org.springframework.web.client.RestTemplate;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    // Esto es un pool de hilos, crea 5 hilos y crece hasta 10 en caso necesario. Cuando un hilo
    // lleva más de 10 minutos sin hacer nada, se elimina.
    private ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 10, TimeUnit.MINUTES, new LinkedBlockingDeque<>());

    private BooksApi booksApi = new BookClient(new RestTemplate(), "http://10.0.2.2:8080");

    private TextView msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        msg = findViewById(R.id.msg);
        // Ejecutamos el loadBooks en otro thread
        executor.submit(() -> loadBooks());
    }


    public void loadBooks() {
        try {

            List<Book> books = booksApi.getBooks(null, null);

            // runOnUiThread(() -> {
            msg.post(() -> {
                for (Book book : books) {
                    msg.append(book.toString());
                    msg.append("\n");
                }
            });

        } catch (Exception e) {
            msg.post(() -> {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                msg.setText(sw.toString());
            });
        }
    }
}