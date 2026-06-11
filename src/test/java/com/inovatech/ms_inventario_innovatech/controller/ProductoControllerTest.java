package com.inovatech.ms_inventario_innovatech.controller;

import com.inovatech.ms_inventario_innovatech.dto.request.ProductoRequest;
import com.inovatech.ms_inventario_innovatech.dto.response.ProductoResponse;
import com.inovatech.ms_inventario_innovatech.entity.Producto;
import com.inovatech.ms_inventario_innovatech.exception.BusinessException;
import com.inovatech.ms_inventario_innovatech.exception.ResourceNotFoundException;
import com.inovatech.ms_inventario_innovatech.service.ProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoControllerTest {

    @Mock
    private ProductoService productoService;

    @InjectMocks
    private ProductoController productoController;

    private ProductoRequest request;
    private Producto producto;

    @BeforeEach
    void setUp() {
        request = new ProductoRequest("Laptop Test", "Laptop para pruebas", 999.99f, 10, "Electrónica");
        producto = new Producto(1L, "Laptop Test", "Laptop para pruebas", 999.99f, 10, "Electrónica");
    }

    @Test
    void testCrearProducto() {
        when(productoService.crearProducto(any(ProductoRequest.class))).thenReturn(producto);

        ResponseEntity<ProductoResponse> response = productoController.crearProducto(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().id());
        assertEquals("Laptop Test", response.getBody().nombre());
        assertEquals(999.99f, response.getBody().precio());
        verify(productoService, times(1)).crearProducto(any(ProductoRequest.class));
    }

    @Test
    void testCrearProductoConNombreDuplicado() {
        when(productoService.crearProducto(any(ProductoRequest.class)))
                .thenThrow(new BusinessException("Ya existe un producto con el nombre: Laptop Test"));

        assertThrows(BusinessException.class, () -> productoController.crearProducto(request));
        verify(productoService, times(1)).crearProducto(any(ProductoRequest.class));
    }

    @Test
    void testObtenerTodosLosProductos() {
        Producto producto2 = new Producto(2L, "Producto 2", "Descripción 2", 200.0f, 10, "Categoría 2");
        List<Producto> productos = Arrays.asList(producto, producto2);
        when(productoService.obtenerTodosLosProductos()).thenReturn(productos);

        ResponseEntity<List<ProductoResponse>> response = productoController.obtenerTodosLosProductos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Laptop Test", response.getBody().get(0).nombre());
        assertEquals("Producto 2", response.getBody().get(1).nombre());
        verify(productoService, times(1)).obtenerTodosLosProductos();
    }

    @Test
    void testObtenerProductoPorId() {
        when(productoService.obtenerProductoPorId(1L)).thenReturn(producto);

        ResponseEntity<ProductoResponse> response = productoController.obtenerProductoPorId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().id());
        assertEquals("Laptop Test", response.getBody().nombre());
        verify(productoService, times(1)).obtenerProductoPorId(1L);
    }

    @Test
    void testObtenerProductoPorIdNoEncontrado() {
        when(productoService.obtenerProductoPorId(999L))
                .thenThrow(new ResourceNotFoundException("Producto no encontrado con ID: 999"));

        assertThrows(ResourceNotFoundException.class, () -> productoController.obtenerProductoPorId(999L));
        verify(productoService, times(1)).obtenerProductoPorId(999L);
    }

    @Test
    void testActualizarProducto() {
        ProductoRequest updateRequest = new ProductoRequest("Producto Actualizado", "Descripción actualizada", 300.0f, 15, "Nueva categoría");
        Producto productoActualizado = new Producto(1L, "Producto Actualizado", "Descripción actualizada", 300.0f, 15, "Nueva categoría");
        when(productoService.actualizarProducto(eq(1L), any(ProductoRequest.class))).thenReturn(productoActualizado);

        ResponseEntity<ProductoResponse> response = productoController.actualizarProducto(1L, updateRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().id());
        assertEquals("Producto Actualizado", response.getBody().nombre());
        assertEquals(300.0f, response.getBody().precio());
        verify(productoService, times(1)).actualizarProducto(eq(1L), any(ProductoRequest.class));
    }

    @Test
    void testEliminarProducto() {
        doNothing().when(productoService).eliminarProducto(1L);

        ResponseEntity<Void> response = productoController.eliminarProducto(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(productoService, times(1)).eliminarProducto(1L);
    }
}
