package com.inovatech.ms_inventario_innovatech.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.inovatech.ms_inventario_innovatech.dto.request.ProductoRequest;
import com.inovatech.ms_inventario_innovatech.entity.Producto;
import com.inovatech.ms_inventario_innovatech.exception.BusinessException;
import com.inovatech.ms_inventario_innovatech.exception.ResourceNotFoundException;
import com.inovatech.ms_inventario_innovatech.repository.ProductoRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    @Test
    void crearProducto_fallaSiNombreYaExiste() {
        ProductoRequest request = new ProductoRequest("Notebook Pro", "Equipo de prueba", 999990f, 5, "Computacion", "https://example.com/notebook.jpg");
        when(productoRepository.existsByNombre("Notebook Pro")).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class, () -> productoService.crearProducto(request));

        assertEquals("Ya existe un producto con el nombre: Notebook Pro", exception.getMessage());
    }

    @Test
    void crearProducto_guardaEntidadConDatosDelRequest() {
        ProductoRequest request = new ProductoRequest("Monitor QA", "Monitor 27 pulgadas", 249990f, 8, "Perifericos", "https://example.com/monitor.jpg");
        Producto saved = new Producto(7L, "Monitor QA", "Monitor 27 pulgadas", 249990f, 8, "Perifericos", "https://example.com/monitor.jpg");

        when(productoRepository.existsByNombre("Monitor QA")).thenReturn(false);
        when(productoRepository.save(any(Producto.class))).thenReturn(saved);

        Producto result = productoService.crearProducto(request);

        ArgumentCaptor<Producto> captor = ArgumentCaptor.forClass(Producto.class);
        verify(productoRepository).save(captor.capture());
        assertEquals("Monitor QA", captor.getValue().getNombre());
        assertEquals(249990f, captor.getValue().getPrecio());
        assertEquals("https://example.com/monitor.jpg", captor.getValue().getImagenUrl());
        assertEquals(7L, result.getId());
    }

    @Test
    void obtenerProductoPorId_fallaSiNoExiste() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> productoService.obtenerProductoPorId(99L));

        assertEquals("Producto no encontrado con ID: 99", exception.getMessage());
    }
}
