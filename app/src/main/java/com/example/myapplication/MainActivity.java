package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.iesfm.library.Book;
import org.iesfm.library.client.BookClient;
import org.iesfm.library.client.BooksApi;
import org.springframework.web.client.RestTemplate;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Book> books;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
               test();
            }
        });
        t.start();
        try {
            t.join();
        } catch (Exception e1) {

        }
        TextView msg = findViewById(R.id.msg);


        for (Book book : books) {
            msg.append(book.toString());
        }

    }


    public void test() {
        TextView msg = findViewById(R.id.msg);
        try {
            BooksApi booksApi = new BookClient(new RestTemplate(), "http://10.0.2.2:8080");

            books = booksApi.getBooks(null, null);


        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            msg.setText(sw.toString());
            try {
                Thread.sleep(1000000000);
            } catch (Exception e1) {

            }
        }
    }
}