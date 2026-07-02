# ms-inventario-innovatech

## Estado de evidencia

| Categoria | Estado |
| --- | --- |
| Implementado | CRUD de productos, DTOs, `imagenUrl`, JWT, Actuator |
| Configurado | MySQL, perfiles `local/aws`, Docker |
| Validado | compilacion |
| Pendiente runtime | consumo real via Gateway/BFF y RDS/AWS |
| No evidenciado | RabbitMQ |

## 1. Descripcion general

Microservicio encargado del catalogo de productos e inventario. Permite crear, listar, obtener, actualizar y eliminar productos, incluyendo soporte para `imagenUrl`.

## 2. Rol dentro de la arquitectura

- API Gateway: entrada oficial para `/api/v1/productos/**`.
- BFF: puede consumir informacion para dashboards agregados.
- Persistencia: MySQL propia.
- RabbitMQ: no se evidencia uso.

Flujo base:

`Frontend -> API Gateway -> Inventario -> MySQL`

## 3. Stack tecnico

- Java 21
- Spring Boot 3.5.14
- Maven Wrapper
- Spring Web
- Spring Data JPA
- Spring Security
- JWT
- MySQL
- Spring Boot Actuator
- Springdoc OpenAPI
- Docker

## 4. Puerto y exposicion

| Item | Valor |
| --- | --- |
| Puerto interno | `8087` |
| Configuracion | `${SERVER_PORT:8087}` |
| Exposicion publica oficial | via API Gateway |
| Exposicion directa recomendada | no |

## 5. Perfiles soportados

| Perfil | Uso | Estado |
| --- | --- | --- |
| `local` | Docker local / desarrollo | Configurado |
| `aws` | ECS Fargate + RDS | Configurado |

Notas:

- El perfil por defecto es `local`.
- `ddl-auto` se mantiene en `update`.
- El `Dockerfile` actual ya esta alineado con el puerto `8087`.

## 6. Variables de entorno requeridas

### Comunes

| Variable | Uso |
| --- | --- |
| `SERVER_PORT` | puerto HTTP |
| `JWT_SECRET` | secreto JWT |
| `APP_SECURITY_DOCS_PUBLIC` | habilita docs publicas en local |
| `INVENTARIO_DEMO_PRODUCTS_ENABLED` | habilita seed demo local |

### Base de datos

| Variable | Local | AWS |
| --- | --- | --- |
| `DB_HOST` | opcional, default `localhost` | requerida |
| `DB_PORT` | opcional, default `3306` | requerida |
| `DB_NAME` | opcional, default `innovatech_inventario` | requerida |
| `DB_USERNAME` | opcional | requerida |
| `DB_PASSWORD` | opcional | requerida |

Compatibilidad adicional:

- `INVENTARIO_MYSQL_HOST`
- `INVENTARIO_MYSQL_PORT`
- `INVENTARIO_MYSQL_DATABASE`
- `INVENTARIO_MYSQL_USERNAME`
- `INVENTARIO_MYSQL_PASSWORD`

## 7. Endpoints principales

| Metodo | Ruta | Uso |
| --- | --- | --- |
| `GET` | `/api/v1/productos` | listar productos |
| `GET` | `/api/v1/productos/{id}` | obtener producto |
| `POST` | `/api/v1/productos` | crear producto |
| `PUT` | `/api/v1/productos/{id}` | actualizar producto |
| `DELETE` | `/api/v1/productos/{id}` | eliminar producto |
| `GET` | `/actuator/health` | healthcheck |

## 8. Integracion y dependencias

| Componente | Tipo | Estado |
| --- | --- | --- |
| API Gateway | HTTP entrante | Evidenciado |
| BFF | consumo interno potencial | Evidenciado por configuracion |
| MySQL | persistencia | Evidenciado |
| RabbitMQ | eventos | No evidenciado |

## 9. Docker y build

- `Dockerfile` presente y validado.
- Imagen preparada para `SPRING_PROFILES_ACTIVE=aws` por defecto en contenedor.
- El servicio local via `docker-compose` usa perfil `local`.

Comandos utiles:

```bash
./mvnw.cmd -q -DskipTests compile
docker build -t innovatech-inventario .
```

## 10. Estado actual de validacion

- `Validado`: compilacion.
- `Configurado`: perfiles `local/aws`, Docker, variables `DB_*`.
- `Pendiente runtime`: validacion integral en AWS y consumo funcional extremo a extremo con frontend.
