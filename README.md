# Android test

Este proyecto contiene un ejemplo de cómo consumir el backend REST desde una app desarrollada en Android. Este ejemplo se conecta con el backend de LibraryProject.

Para poder llevar a cabo la comunicación entre la app y el servicio REST se ha utilizado la librería Retrofit2.

## Introducción


Lo primero que hay que tener en cuenta, es que Android tiene un modelo de concurrencia según el cual no se pueden
realizar operaciones de entrada salida (acceso a bases de datos, acceso a servicios REST, etc...) desde el hilo
principal de la app (conocido como hilo UI).

Esta restricción se debe a que ese hilo debe encargarse únicamente de pintar el interfaz gráfico de la app, realizando
cambios cuando se requiera.

Debido a esta restricción se hace necesario utilizar librerías asíncronas* para la realización de operaciones de entrada/salida.

### Asincronía

Decimos que una librería es asíncrona cuando nos permite ejecutar acciones de entrada/salida sin bloquear la ejecución del hilo que ha ordenado la ejecución de la acción.

Cuando se invoca un método asíncrono, es necesario definir el comportamiento del programa cuando llegue la respuesta. Para esto existen los callbacks..

### Retrofit2

Retrofit2 es una librería que nos permite construir clientes REST asíncronos. En este respositorio podéis ver un ejemplo de como llamar al backend y procesar la respuesta de manera asíncrona.

## Configuración del proyecto

Para poder utilizar la librería Retrofit2 es necesario añadir estas dos dependencias en el fichero build.gradle

```gradle
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-jackson:2.9.0'
```

Además, en este ejemplo  se ha añadido la dependencia del modelo de LibraryProject con el objetivo de poder usar los POJOs ahí definidos. Para poder cargar esta dependencia es necesario haber construido LibraryProject previamente.

```bash
cd LibraryProject
mvn clean install
```

Una vez hemos construido LibraryProject, debemos añadir a gradle el repositorio local de maven. Para ello debemos añadir mavenLocal() al dependencyManagement del fichero settings.gradle

```gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        mavenLocal()
    }
}
```

Por último, ya podemos añadir la dependencia en build.gradle

```gradle
      implementation 'org.iesfm:library-model:1.1-SNAPSHOT'
```

## Ejemplo de invocación al backend

Dentro de MainActivity podemos encontrar un ejemplo de cómo llamar al endpoint GET /books

### BookClient

Por cada controlador del backend debemos crear un interface similar que nos permita realizar las llamadas al backend de manera asíncrona.

Todos los métodos del BookClient deben devolver un objeto de tipo `Call<T>`, donde T será el return type del método que se está invocando en el backend. Por ejemplo, si queremos invocar al método `List<Book> list()` del backend el métode BookClient debe ser `Call<List<Book>> list()`.


### Construcción de BookClient

Para poder construir un cliente primero necesitamos configurar Retrofit2, en este caso vamos a utilizar Jackson para la serialización/deserialización.

```java
Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        // Se crea el cliente de la api de libros. Con este objeto realizaremos
        // las llamadas al backend
        booksClient = retrofit.create(BookClient.class);
```

### Invocación al backend y procesamiento de la respuesta

Ahora que ya tenemos un objeto de tipo BookCliente es posible realizar una invocación de la siguiente manera


```java
booksClient.createBook(new Book("1fdsfds", "Otro", "AAA", 1900)).enqueue(
                new Callback<>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.code() == 409) {
                            msg.append("Duplicado\n");

                        } else {
                            msg.append("Libro creado\n");
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("MainActivity", t.getMessage(), t);                    }
                }
        );
```

Este código realiza una llamada asíncrona al backend gracias al método `enqueue` que viene después de la invocación concreta. Hay que crear un objeto Callback que nos permite definir qué debe hacer el programa cuando recibe una respuesta del backend.

El método onResponse nos permite definir qué hacer cuando el backend responde de alguna manera. Podemos usar `response.code()` para saber qué código de respuesta ha enviado el backend.


Por otro lado, si la comunicación ha fallado por la razón que sea se invocará el método onFailure, en este caso lo más recomendable es mostrar la excepción en el log del teléfono como se muestra en el ejemplo.