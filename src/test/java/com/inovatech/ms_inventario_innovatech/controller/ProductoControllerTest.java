package com.inovatech.ms_inventario_innovatech.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.inovatech.ms_inventario_innovatech.entity.Producto;
import com.inovatech.ms_inventario_innovatech.service.ProductoService;

class ProductoControllerTest {

    @Mock
    private ProductoService productoService;

    @InjectMocks
    private ProductoController productoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearProducto() {
        // Arrange
        Producto producto = new Producto();
        producto.setNombre("Laptop Test");
        producto.setDescripcion("Laptop para pruebas");
        producto.setPrecio(999.99f);
        producto.setStock(10);
        producto.setCategoria("Electrónica");

        Producto productoGuardado = new Producto();
        productoGuardado.setId(1L);
        productoGuardado.setNombre("Laptop Test");
        productoGuardado.setDescripcion("Laptop para pruebas");
        productoGuardado.setPrecio(999.99f);
        productoGuardado.setStock(10);
        productoGuardado.setCategoria("Electrónica");

        when(productoService.crearProducto(any(Producto.class))).thenReturn(productoGuardado);

        // Act
        ResponseEntity<Producto> response = productoController.crearProducto(producto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("Laptop Test", response.getBody().getNombre());
        assertEquals(999.99f, response.getBody().getPrecio());
        verify(productoService, times(1)).crearProducto(any(Producto.class));
    }

    @Test
    void testObtenerTodosLosProductos() {
        // Arrange
        Producto producto1 = new Producto();
        producto1.setId(1L);
        producto1.setNombre("Producto 1");
        producto1.setDescripcion("Descripción 1");
        producto1.setPrecio(100.0f);
        producto1.setStock(5);
        producto1.setCategoria("Categoría 1");

        Producto producto2 = new Producto();
        producto2.setId(2L);
        producto2.setNombre("Producto 2");
        producto2.setDescripcion("Descripción 2");
        producto2.setPrecio(200.0f);
        producto2.setStock(10);
        producto2.setCategoria("Categoría 2");

        List<Producto> productos = Arrays.asList(producto1, producto2);
        when(productoService.obtenerTodosLosProductos()).thenReturn(productos);

        // Act
        ResponseEntity<List<Producto>> response = productoController.obtenerTodosLosProductos();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Producto 1", response.getBody().get(0).getNombre());
        assertEquals("Producto 2", response.getBody().get(1).getNombre());
        verify(productoService, times(1)).obtenerTodosLosProductos();
    }

    @Test
    void testObtenerProductoPorId() {
        // Arrange
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Producto Test");
        producto.setDescripcion("Descripción Test");
        producto.setPrecio(150.0f);
        producto.setStock(8);
        producto.setCategoria("Test");

        when(productoService.obtenerProductoPorId(1L)).thenReturn(Optional.of(producto));

        // Act
        ResponseEntity<Producto> response = productoController.obtenerProductoPorId(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("Producto Test", response.getBody().getNombre());
        verify(productoService, times(1)).obtenerProductoPorId(1L);
    }

    @Test
    void testObtenerProductoPorIdNoEncontrado() {
        // Arrange
        when(productoService.obtenerProductoPorId(999L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Producto> response = productoController.obtenerProductoPorId(999L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(productoService, times(1)).obtenerProductoPorId(999L);
    }

    @Test
    void testActualizarProducto() {
        // Arrange
        Producto productoActualizado = new Producto();
        productoActualizado.setNombre("Producto Actualizado");
        productoActualizado.setDescripcion("Descripción actualizada");
        productoActualizado.setPrecio(300.0f);
        productoActualizado.setStock(15);
        productoActualizado.setCategoria("Nueva categoría");

        Producto productoResultado = new Producto();
        productoResultado.setId(1L);
        productoResultado.setNombre("Producto Actualizado");
        productoResultado.setDescripcion("Descripción actualizada");
        productoResultado.setPrecio(300.0f);
        productoResultado.setStock(15);
        productoResultado.setCategoria("Nueva categoría");

        when(productoService.actualizarProducto(eq(1L), any(Producto.class))).thenReturn(productoResultado);

        // Act
        ResponseEntity<Producto> response = productoController.actualizarProducto(1L, productoActualizado);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("Producto Actualizado", response.getBody().getNombre());
        assertEquals(300.0f, response.getBody().getPrecio());
        verify(productoService, times(1)).actualizarProducto(eq(1L), any(Producto.class));
    }

    @Test
    void testEliminarProducto() {
        // Arrange
        doNothing().when(productoService).eliminarProducto(1L);

        // Act
        ResponseEntity<Void> response = productoController.eliminarProducto(1L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(productoService, times(1)).eliminarProducto(1L);
    }

    @Test
    void testCrearProductoConNombreDuplicado() {
        // Arrange
        Producto producto = new Producto();
        producto.setNombre("Laptop Duplicada");
        producto.setDescripcion("Laptop duplicada");
        producto.setPrecio(999.99f);
        producto.setStock(10);
        producto.setCategoria("Electrónica");

        when(productoService.crearProducto(any(Producto.class)))
                .thenThrow(new IllegalArgumentException("Ya existe un producto con el nombre: Laptop Duplicada"));

        // Act
        ResponseEntity<Producto> response = productoController.crearProducto(producto);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNull(response.getBody());
        verify(productoService, times(1)).crearProducto(any(Producto.class));
    }
}
