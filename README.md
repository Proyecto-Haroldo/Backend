# Backend 

Pequeño readme con los endpoints que hay hasta ahorita.

Pa correrlo instalan Java 21 y Maven. O ahí está el docker compose bien querido montan el contenedor y lo corren ahí. Sale igual en el puerto 8080.

## Endpoints

Todos los endpoints tienen el prefijo /api y después el controlador.

Por ejemplo, http://localhost:8080/api/{controlador}/{endpoint} 

## WebQuestionController ❔(/preguntas)

Pa las preguntas de la web

### GET

Trae una lista de todas las preguntas.

### GET /{id}

Trae una pregunta según su ID en la BD según este formato:

{

  id: number;

  title: string;

  description: string;

  type: QuestionType;

  options?: QuestionOption[];

  keywords: Keyword[];

}

### GET /categoriaycliente
    http://localhost:8080/api/preguntas/categoriaycliente

Tiene dos parámetros de query:

category | Nombre de la categoría

clienttype | Tipo del cliente

Un ejemplo de request pa acá:

    http://localhost:8080/api/preguntas/categoriaycliente?category=estrategia&clienttype=empresa

### POST

Agrega una pregunta a la base de datos y sus opciones (se debe envíar bajo el mismo modelo en que se recibe)

## PD

![Nosotros](https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ40u6SyPgW-mxI1yzXj6beZQ0DGFGj2Oi4dA&s)