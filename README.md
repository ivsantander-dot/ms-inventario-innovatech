# ms-inventario-innovatech

## Estado de evidencia

| Categoria | Estado |
|---|---|
| Implementado | CRUD de productos, DTOs, errores |
| Configurado | MySQL/H2, perfiles, Docker |
| Validado | compilacion |
| Pendiente de validacion runtime | consumo real via Gateway y stack completo |
| No evidenciado | RabbitMQ |

## 1. Descripcion general

Microservicio encargado de productos e inventario. Permite crear, listar, obtener, actualizar y eliminar productos.

## 2. Rol dentro de la arquitectura

- API Gateway: recibe trafico oficial desde `/api/v1/productos/**`.
- BFF: puede ser consumido por el BFF para dashboards agregados.
- Otros microservicios: No evidenciado como consumidor HTTP saliente.
- Base de datos: H2 en desarrollo y MySQL en produccion.
- RabbitMQ: No evidenciado.

Flujo simple:

`Cliente/Frontend -> API Gateway -> Inventario -> Base de datos`

## 3. Stack tecnico

- Java 21
- Spring Boot 3.5.14
- Maven
- Spring Web
- Spring Data JPA
- Spring Security
- JWT
- H2
- MySQL
- Actuator
- Swagger/OpenAPI
- Docker

## 4. Puerto del servicio

| Concepto | Valor |
|---|---|
| Puerto esperado | `8087` |
| Puerto configurado | `${SERVER_PORT:8087}` |
| Archivo donde se define | `src/main/resources/application.properties` |
| Variable de entorno asociada | `SERVER_PORT` |

## 5. Variables de entorno

| Variable | Descripcion | Valor por defecto | Obligatoria | Riesgo/observacion |
|---|---|---|---|---|
| `SERVER_PORT` | Puerto HTTP del servicio | `8087` | No | Debe alinearse con Gateway/Docker |
| `JWT_SECRET` | Secreto para validar JWT | No evidenciado en default | Si | Critica |
| `APP_SECURITY_DOCS_PUBLIC` | Control de docs publicas | `false` en base, `true` en dev | No | No abrir en prod |
| `INVENTARIO_MYSQL_HOST` | Host MySQL prod | No default en prod | Si en prod | Debe existir |
| `INVENTARIO_MYSQL_PORT` | Puerto MySQL prod | No default en prod | Si en prod | Debe existir |
| `INVENTARIO_MYSQL_DATABASE` | Base prod | No default en prod | Si en prod | Debe existir |
| `INVENTARIO_MYSQL_USERNAME` | Usuario DB prod | No default en prod | Si en prod | No usar root |
| `INVENTARIO_MYSQL_PASSWORD` | Password DB prod | No default en prod | Si en prod | Sensible |
| `INVENTARIO_DEMO_PRODUCTS_ENABLED` | Inserta 10 productos demo idempotentes | `false` | No | Solo local/desarrollo; no activar en produccion |

## 6. Base de datos

| Elemento | Valor |
|---|---|
| Motor | H2 en dev, MySQL en prod |
| Base de datos | `inventario` en H2 dev, `${INVENTARIO_MYSQL_DATABASE}` en prod |
| Entidades | `Producto` |
| Repositories | `ProductoRepository` |
| ddl-auto | `update` en dev, `validate` en prod |
| show-sql | `true` en dev, `false` en prod |

Riesgos o pendientes:

- El `Dockerfile` expone `8080`, pero las propiedades del servicio usan `8087`.
- La politica publica de lectura depende del Gateway y `SecurityConfig`.

## 7. Endpoints principales

| Metodo | Endpoint | Descripcion | Auth requerida | Request | Response |
|---|---|---|---|---|---|
| `POST` | `/api/v1/productos` | Crea un producto | Si | `ProductoRequest` | `ProductoResponse` |
| `GET` | `/api/v1/productos` | Lista productos | No en Gateway para lectura; politica local segun `SecurityConfig` | No aplica | `List<ProductoResponse>` |
| `GET` | `/api/v1/productos/{id}` | Busca producto por id | No en Gateway para lectura; politica local segun `SecurityConfig` | No aplica | `ProductoResponse` |
| `PUT` | `/api/v1/productos/{id}` | Actualiza producto | Si | `ProductoRequest` | `ProductoResponse` |
| `DELETE` | `/api/v1/productos/{id}` | Elimina producto | Si | No aplica | `204 No Content` |
| `GET` | `/api/v1/productos/health` | Health funcional propio | Pendiente de verificacion | No aplica | No evidenciado |

## 8. Seguridad

- Usa Spring Security: Si.
- Valida JWT: Si.
- Depende del Gateway: No estrictamente; puede validar acceso directo.
- Endpoints publicos: lectura de productos segun arquitectura; validar con `SecurityConfig`.
- Endpoints protegidos: escritura y borrado de productos.
- Riesgos detectados:
  - `Dockerfile` expone un puerto distinto al configurado.
  - El endpoint `/health` propio existe, pero su politica exacta debe verificarse en `SecurityConfig`.

## 9. Integraciones

| Origen | Destino | Tipo | URL/variable | Estado |
|---|---|---|---|---|
| Gateway | Inventario | HTTP | `/api/v1/productos/**` | Evidenciado |
| BFF | Inventario | HTTP | No evidenciado aqui; esperado por arquitectura | Pendiente de verificacion |
| Inventario | Base de datos | JPA/JDBC | H2 dev / `INVENTARIO_MYSQL_*` prod | Evidenciado |

## 10. Eventos RabbitMQ

No se evidencian eventos RabbitMQ en este microservicio.

## 11. Ejecucion local

```bash
./mvnw clean package
./mvnw spring-boot:run
```

Pruebas basicas:

```bash
curl http://localhost:8087/api/v1/productos
```

## 12. Datos demo locales

El servicio incluye un seeder idempotente de 10 productos de prueba para validar catalogo cliente y CRUD administrativo.

- Activacion: `INVENTARIO_DEMO_PRODUCTS_ENABLED=true`.
- Alcance: entorno local/desarrollo o Docker local.
- Seguridad: no contiene datos sensibles y no debe activarse en produccion.
- Idempotencia: no duplica productos si ya existe un producto con el mismo `nombre`.
