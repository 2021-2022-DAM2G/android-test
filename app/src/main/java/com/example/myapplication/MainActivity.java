package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.iesfm.library.Book;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class MainActivity extends AppCompatActivity {


    private BookClient booksClient;

    private TextView msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Creamos Retrofit que va a acceder a la ip de la máquina host
        // Este retrofit va a usar jackson para la conversión Json/clases
        // Se va a conectar a la ip del host (siempre es 10.0.2.2)
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        // Se crea el cliente de la api de libros. Con este objeto realizaremos
        // las llamadas al backend
        booksClient = retrofit.create(BookClient.class);

        setContentView(R.layout.activity_main);
        msg = findViewById(R.id.msg);
        // Invocamos el método encargado de cargar los libros en pantalla
        loadBooks();
    }


    public void loadBooks() {
        // Realizamos una llamada asíncrona (en otro hilo) al backend
        // Las llamadas al backend deben realizarse de manera asíncrona
        // obligatoriamente, debido al modelo de concurrencia de Android
        booksClient.getAllBooks().enqueue(
                new Callback<>() {
                    /**
                     * Este métodos se ejecuta cuando el backend responde sin errores
                     * @param call Representa la llamada que se hizo
                     * @param response La respuesta del backend (dentro está el body)
                     */
                    @Override
                    public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                        // Obtenemos los libros del cuerpo de la respuesta
                        List<Book> books = response.body();
                        for (Book book : books) {
                            msg.append(book.toString());
                            msg.append("\n");
                        }
                    }

                    /**
                     * Este método se ejecuta cuando se produce algún error en la
                     * comunicación con el backend
                     * @param call La llamada al backend
                     * @param t El error que se ha producido
                     */
                    @Override
                    public void onFailure(Call<List<Book>> call, Throwable t) {
                        // Mostramos el error en el log del teléfono
                        Log.e("MainActivity", t.getMessage(), t);
                    }
                }
        );
    }
}