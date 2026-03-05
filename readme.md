# MatchService

Microservicio de gestión de partidos deportivos con arquitectura hexagonal.

## Descripción

MatchService es un microservicio Spring Boot que permite gestionar partidos deportivos, sincronizarlos desde la API externa SportMonks, y exponerlos mediante una API REST. Implementa arquitectura hexagonal (Ports & Adapters) para mantener el dominio libre de dependencias de infraestructura.

### Características principales

- Gestión de partidos: creación, actualización de marcadores, cambios de estado
- Máquina de estados completa para transiciones de partido
- Sincronización con API SportMonks con retry automático
- Cacheo con ValkeyDB para optimizar consultas
- Publicación de eventos via Kafka
- Arquitectura hexagonal limpia y testeable

## Endpoints

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/v1/matches` | Lista todos los partidos |
| GET | `/api/v1/matches/{id}` | Obtiene un partido por ID |
| PUT | `/api/v1/matches/{id}/score` | Actualiza el marcador |
| PUT | `/api/v1/matches/{id}/status` | Cambia el estado del partido |
| PUT | `/api/v1/matches/{id}/confirm` | Confirma el resultado |
| GET | `/api/v1/matches/league/{league}` | Filtra por liga |
| GET | `/api/v1/matches/dates` | Filtra por rango de fechas |
| GET | `/api/v1/matches/status/{status}` | Filtra por estado |
| GET | `/api/v1/matches/team/{teamName}` | Filtra por equipo |
| GET | `/api/v1/matches/{id}/exists` | Verifica si existe |
| GET | `/api/v1/matches/{id}/can-update` | Verifica si es editable |

## Tecnologías

- **Java 17** - Lenguaje de programación
- **Spring Boot 3.4.5** - Framework principal
- **Spring Data JPA** - Persistencia con PostgreSQL
- **ValkeyDB** - Cacheo reactivo (API compatible con Redis)
- **Spring Kafka** - Publicación de eventos
- **Eureka Client** - Registro en service discovery
- **Maven** - Gestión de dependencias

## Arquitectura

Este proyecto sigue **Arquitectura Hexagonal** (Ports & Adapters):

```
MatchService/
├── src/main/java/com/ProdeMaster/MatchService/
│   ├── application/          # Casos de uso, DTOs, servicios de aplicación
│   │   ├── dto/
│   │   │   ├── request/      # Request DTOs
│   │   │   └── response/     # Response DTOs
│   │   ├── port/in/          # Puertos de entrada
│   │   └── service/          # Servicios de aplicación
│   ├── domain/               # Lógica de negocio pura
│   │   ├── model/            # Entidades de dominio (Match, MatchStatus)
│   │   ├── exception/        # Excepciones de dominio
│   │   └── projection/       # Proyecciones
│   └── infraestructure/      # Adaptadores externos
│       ├── adapter/out/
│       │   ├── sportmonks/   # Adaptador SportMonks API
│       │   │   └── dto/      # Modelos de respuesta SportMonks
│       │   └── cache/        # Implementación de cache
│       ├── api/              # Controladores REST
│       ├── config/           # Configuraciones
│       ├── event/            # Publicadores de eventos
│       ├── persistence/      # Repositorios JPA
│       └── scheduler/        # Tareas programadas
└── src/main/resources/
    ├── application.properties
    ├── schema.sql
    └── data.sql
```

### Capas

- **application**: Casos de uso y orquestación. Usa DTOs para entrada/salida.
- **domain**: Lógica de negocio pura, sin dependencias de Spring.
- **infraestructure**: Adaptadores para sistemas externos (BD, API, cache, Kafka).

## Estados de Partido

El dominio implementa una máquina de estados completa:

| Estado | Descripción |
|--------|-------------|
| `PENDING` | Pendiente |
| `TBA` | Por anunciar |
| `NS` | No iniciado |
| `INPLAY_1ST_HALF` | Primer tiempo en juego |
| `HT` | Descanso |
| `INPLAY_2ND_HALF` | Segundo tiempo en juego |
| `FT` | Tiempo completo |
| `INPLAY_ET` | Prórroga en juego |
| `EXTRA_TIME_BREAK` | Descanso de prórra |
| `INPLAY_ET_2ND_HALF` | Segunda parte de prórra |
| `AET` | Después de prórra |
| `INPLAY_PENALTIES` | Penales en juego |
| `PEN_BREAK` | Descanso de penales |
| `FT_PEN` | Después de penales |
| `DELAYED` | Retrasado |
| `POSTPONED` | Aplazado |
| `SUSPENDED` | Suspendido |
| `INTERRUPTED` | Interrumpido |
| `AWARDED` | Otorgado |
| `WO` | W.O. |
| `CANCELLED` | Cancelado |
| `ABANDONED` | Abandonado |
| `DELETED` | Eliminado |
| `AWAITING_UPDATES` | Esperando actualizaciones |

## Configuración

```properties
# Puerto del servicio
server.port=8083

# SportMonks API
sportmonks.baseUrl=https://api.sportmonks.com/v3
sportmonks.token=${SPORTMONKS_TOKEN}

# Eureka
eureka.client.serviceUrl.defaultZone=http://localhost:8761/eureka/

# Valkey (cache)
spring.data.redis.host=localhost
spring.data.redis.port=6379

# Kafka
spring.kafka.bootstrap-servers=localhost:9092
app.kafka.topic.match-events=match.events

# Zipkin (trazabilidad)
spring.zipkin.base-url=http://localhost:9411
```

## Ejecutar en local

```bash
# Compilar
./mvnw clean package

# Ejecutar
./mvnw spring-boot:run

# Con perfil específico
./mvnw spring-boot:run -Dspring.profiles.active=dev
```

## Tests

```bash
# Todos los tests
./mvnw test

# Test específico
./mvnw test -Dtest=MatchTest

# Con reporte
./mvnw test surefire-report:report
```

## Linting

```bash
# Checkstyle
./mvnw checkstyle:check

# SpotBugs
./mvnw spotbugs:check
```

## Sincronización con SportMonks

El servicio sincroniza partidos desde SportMonks con las siguientes características:

- **Retry automático**: 3 intentos con exponential backoff (1s → 2s → 4s)
- **Paginación**: Solo primera página (50 partidos)
- **Scheduler**: Sincronización diaria automática
- **Fallback**: Consultas a SportMonks cuando no hay datos locales

### Modelos SportMonks

```
infraestructure/adapter/out/sportmonks/dto/
├── SportmonksFixtureResponse.java   # Partidos
├── SportmonksStatusResponse.java    # Estados
├── SportmonksTeamResponse.java      # Equipos
├── SportmonksLeagueResponse.java    # Ligas
└── SportmonksApiResponse.java       # Respuesta API
```

## Autor

- **Gastón Herrlein** - [@Gaston-Herrlein](https://github.com/Gaston-Herrlein)

## Licencia

Sin licencia
