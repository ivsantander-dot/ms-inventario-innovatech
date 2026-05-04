# Microservicio de Inventario - MS Inventario Innovatech

## Descripción

Este microservicio es una API REST para la gestión de productos de inventario. Permite realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) sobre productos, con validaciones de datos y persistencia en base de datos H2.

## Tecnologías Utilizadas

- **Spring Boot 4.0.6** - Framework principal
- **Java 21** - Lenguaje de programación
- **Spring Data JPA** - Persistencia de datos
- **H2 Database** - Base de datos en memoria para desarrollo
- **SpringDoc OpenAPI 3.0.2** - Documentación automática de API
- **Lombok** - Reducción de código boilerplate
- **JUnit 5 + Mockito** - Framework de testing

## Endpoints Disponibles

### 1. Crear Producto
```
POST /api/productos
```
**Cuerpo de la petición:**
```json
{
    "nombre": "Laptop Dell XPS 15",
    "descripcion": "Laptop de alto rendimiento con procesador Intel i7, 16GB RAM, 512GB SSD",
    "precio": 1299.99,
    "stock": 25,
    "categoria": "Electrónica"
}
```

### 2. Obtener Todos los Productos
```
GET /api/productos
```

### 3. Obtener Producto por ID
```
GET /api/productos/{id}
```

### 4. Actualizar Producto
```
PUT /api/productos/{id}
```
**Cuerpo de la petición:**
```json
{
    "nombre": "Laptop Dell XPS 15 Actualizada",
    "descripcion": "Laptop de alto rendimiento con procesador Intel i7, 32GB RAM, 1TB SSD",
    "precio": 1499.99,
    "stock": 20,
    "categoria": "Electrónica Premium"
}
```

### 5. Eliminar Producto
```
DELETE /api/productos/{id}
```

## Validaciones

Todos los campos son obligatorios y tienen las siguientes validaciones:

- **nombre**: Obligatorio, máximo 100 caracteres
- **descripcion**: Obligatorio, máximo 500 caracteres
- **precio**: Obligatorio, no puede ser negativo (Float)
- **stock**: Obligatorio, no puede ser negativo (Integer)
- **categoria**: Obligatorio, máximo 50 caracteres

## Ejecución del Proyecto

### Prerrequisitos
- Java 21 o superior
- Maven 3.6 o superior

### Ejecutar la aplicación
```bash
# Compilar y ejecutar
./mvnw.cmd spring-boot:run
```

La aplicación se iniciará en `http://localhost:8080`

### Documentación de la API
Una vez iniciada la aplicación, puedes acceder a:

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI Docs**: `http://localhost:8080/api-docs`
- **H2 Console**: `http://localhost:8080/h2-console`
  - **JDBC URL**: `jdbc:h2:mem:inventario`
  - **Usuario**: `sa`
  - **Contraseña**: `password`

## Ejemplos de Uso con Postman

### Configuración básica
- **Base URL**: `http://localhost:8080/api/productos`
- **Headers**: `Content-Type: application/json`

