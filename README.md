# Backend 

Pequeño readme con los endpoints que hay hasta ahorita.

Pa correrlo instalan Java 21 y Maven. O ahí está el docker compose bien querido montan el contenedor y lo corren ahí. Sale igual en el puerto 8080.

## Endpoints

Todos los endpoints tienen el prefijo /api y después el controlador.

Por ejemplo, http://localhost:8080/api/{controlador}/{endpoint} 

## WebQuestionController ❔(/preguntas)

Este devuelve las preguntas que hay en la base de datos como el modelo que pidió Johann pa que encajara con el front.

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

## WordsController 📖

Este devuelve las palabras clave con su significado (no implementado aún con las preguntas)

### GET

Devuelve una lista de String que son todas las claves que hay en el Redis.

### GET /{word}

Trae la palabra y el significado de dicha palabra como:

{
  title: string,
  description: string
}
