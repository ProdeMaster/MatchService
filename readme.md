# 🌐 Match Service

---

## 🚀 ¿Qué hace este servicio?

---

## 📆 Endpoints destacados

| Método | Ruta     | Descripción |
|--------|----------|-------------|
| GET    | /matches | ...         |

## 🛠️ Tecnologías

---

## 📁 Estructura del proyecto

Para este servicio ya que es el "cerebro" del proyecto, y dispone de una mayor complejidad se decidió aplicar una arquitectura hexagonal que permita mayor organización y desacoplamiento de componentes

```plaintext
MatchService/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/ProdeMaster/MatchService/
│   │   │       ├── application/
│   │   │       │   ├── dto/
│   │   │       │   ├── mapper/
│   │   │       │   └── service/
│   │   │       ├── domain/
│   │   │       │   ├── event/
│   │   │       │   ├── model/
│   │   │       │   ├── repository/
│   │   │       │   └── service/
│   │   │       ├── infraestructure/
│   │   │       │   ├── api/
│   │   │       │   │   └── dto/
│   │   │       │   ├── cache/
│   │   │       │   ├── config/
│   │   │       │   ├── db/
│   │   │       │   └── event/
│   │   │       ├── interfaces/
│   │   │       │   ├── consumer
│   │   │       │   ├── rest
│   │   │       │   └── scheduler
│   │   │       └── MatchServiceApplication.java
│   │   └── resources/
│   │       ├── static/
│   │       ├── templates/
│   │       └── application.properties
│   └── test/java/com/ProdeMaster/MatchService/
│       └── MatchServiceApplicationTests.java
├── Dockerfile
├── pom.xml
└── readme.md
```

#### Descripción de carpetas
* __application:__ Aquí viven los _casos de uso_. Ejemplo: “Obtener partidos de hoy”. Esta capa orquesta el dominio y los adaptadores. Se usan DTOs para la entrada/salida, pero nunca habla en términos de Mongo ni de Kafka.
* __domain:__ Solo contiene la lógica de negocio. Aquí se define `Match` , `Team`, `MatchRepository` (interfaz), `MatchDomainService`, etc. Nada de Spring, nada de Kafka, nada de Mongo.
* __infraestructure:__ Todo lo concreto:
  * Cliente REST para SportMonks. 
  * Implementación `MongoMatchRepository` que implementa `MatchRepository`. 
  * `KafkaMatchPublisher` que implementa `MatchEventPublisher`. 
  * Configuración de beans (`MongoTemplate`, `KafkaProducer`, `ValkeyCacheManager`, etc.).
* __interfaces:__ Son las entradas al microservicio. Ejemplo:
  * `MatchController` expone endpoints REST. 
  * `MatchScheduler` ejecuta tareas cada X minutos. 
  * `MatchConsumer` escucha eventos Kafka.

---

## ⚙️ Configuración

---

## 🧪 Cómo probarlo en local

---

## 📦 Docker

---

## 🧩 Integración con otros servicios

---

## 📚 Documentación adicional

---

## 🧑‍💻 Autor
> Nombre: Gastón Herrlein
>
> GitHub: @Gaston-Herrlein

---

## 📄 Licencia
Sin licencia