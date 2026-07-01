package com.inovatech.ms_inventario_innovatech.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.inovatech.ms_inventario_innovatech.dto.request.ProductoRequest;
import com.inovatech.ms_inventario_innovatech.dto.response.ProductoResponse;
import com.inovatech.ms_inventario_innovatech.entity.Producto;
import com.inovatech.ms_inventario_innovatech.service.ProductoService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
    void crearProducto_retornaProductoCreado() {
        ProductoRequest request = new ProductoRequest("Laptop Test", "Laptop para pruebas", 999.99f, 10, "Electronica", "https://example.com/laptop.jpg");
        Producto productoGuardado = producto(1L, "Laptop Test", 999.99f, 10, "Electronica", "https://example.com/laptop.jpg");

        when(productoService.crearProducto(any(ProductoRequest.class))).thenReturn(productoGuardado);

        ResponseEntity<ProductoResponse> response = productoController.crearProducto(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().id());
        assertEquals("Laptop Test", response.getBody().nombre());
        assertEquals(999.99f, response.getBody().precio());
        assertEquals("https://example.com/laptop.jpg", response.getBody().imagenUrl());
        verify(productoService, times(1)).crearProducto(any(ProductoRequest.class));
    }

    @Test
    void obtenerTodosLosProductos_retornaLista() {
        when(productoService.obtenerTodosLosProductos()).thenReturn(List.of(
                producto(1L, "Producto 1", 100.0f, 5, "Categoria 1", "https://example.com/1.jpg"),
                producto(2L, "Producto 2", 200.0f, 10, "Categoria 2", "https://example.com/2.jpg")));

        ResponseEntity<List<ProductoResponse>> response = productoController.obtenerTodosLosProductos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Producto 1", response.getBody().get(0).nombre());
        assertEquals("Producto 2", response.getBody().get(1).nombre());
        verify(productoService, times(1)).obtenerTodosLosProductos();
    }

    @Test
    void obtenerProductosConStockBajo_retornaListaFiltrada() {
        when(productoService.obtenerProductosConStockMenorA(5)).thenReturn(List.of(
                producto(1L, "Producto Bajo Stock", 100.0f, 3, "Categoria 1", null)));

        ResponseEntity<List<ProductoResponse>> response = productoController.obtenerProductosConStockBajo(5);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(3, response.getBody().get(0).stock());
        verify(productoService, times(1)).obtenerProductosConStockMenorA(5);
    }

    @Test
    void obtenerProductoPorId_retornaProducto() {
        when(productoService.obtenerProductoPorId(1L)).thenReturn(producto(1L, "Producto Test", 150.0f, 8, "Test", "https://example.com/test.jpg"));

        ResponseEntity<ProductoResponse> response = productoController.obtenerProductoPorId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().id());
        assertEquals("Producto Test", response.getBody().nombre());
        verify(productoService, times(1)).obtenerProductoPorId(1L);
    }

    @Test
    void actualizarProducto_retornaProductoActualizado() {
        ProductoRequest request = new ProductoRequest("Producto Actualizado", "Descripcion actualizada", 300.0f, 15, "Nueva categoria", "https://example.com/updated.jpg");
        when(productoService.actualizarProducto(eq(1L), any(ProductoRequest.class)))
                .thenReturn(producto(1L, "Producto Actualizado", 300.0f, 15, "Nueva categoria", "https://example.com/updated.jpg"));

        ResponseEntity<ProductoResponse> response = productoController.actualizarProducto(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().id());
        assertEquals("Producto Actualizado", response.getBody().nombre());
        assertEquals(300.0f, response.getBody().precio());
        assertEquals("https://example.com/updated.jpg", response.getBody().imagenUrl());
        verify(productoService, times(1)).actualizarProducto(eq(1L), any(ProductoRequest.class));
    }

    @Test
    void eliminarProducto_retornaNoContent() {
        doNothing().when(productoService).eliminarProducto(1L);

        ResponseEntity<Void> response = productoController.eliminarProducto(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(productoService, times(1)).eliminarProducto(1L);
    }

    private Producto producto(Long id, String nombre, Float precio, Integer stock, String categoria, String imagenUrl) {
        Producto producto = new Producto();
        producto.setId(id);
        producto.setNombre(nombre);
        producto.setDescripcion("Descripcion " + nombre);
        producto.setPrecio(precio);
        producto.setStock(stock);
        producto.setCategoria(categoria);
        producto.setImagenUrl(imagenUrl);
        return producto;
    }
}
