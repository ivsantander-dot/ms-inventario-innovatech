package com.inovatech.ms_inventario_innovatech.service;

import com.inovatech.ms_inventario_innovatech.dto.request.ProductoRequest;
import com.inovatech.ms_inventario_innovatech.entity.Producto;
import com.inovatech.ms_inventario_innovatech.exception.BusinessException;
import com.inovatech.ms_inventario_innovatech.exception.ResourceNotFoundException;
import com.inovatech.ms_inventario_innovatech.repository.ProductoRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductoService {

    private final ProductoRepository productoRepository;

    public Producto crearProducto(ProductoRequest request) {
        if (productoRepository.existsByNombre(request.nombre())) {
            throw new BusinessException("Ya existe un producto con el nombre: " + request.nombre());
        }
        Producto producto = new Producto(
                null,
                request.nombre(),
                request.descripcion(),
                request.precio(),
                request.stock(),
                request.categoria(),
                normalizeImageUrl(request.imagenUrl()));
        return productoRepository.save(producto);
    }

    public List<Producto> obtenerTodosLosProductos() {
        return productoRepository.findAll();
    }

    public List<Producto> obtenerProductosConStockMenorA(Integer stockMenorA) {
        return productoRepository.findByStockLessThan(stockMenorA);
    }

    public Producto obtenerProductoPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));
    }

    public Producto actualizarProducto(Long id, ProductoRequest request) {
        Producto productoExistente = obtenerProductoPorId(id);
        if (!productoExistente.getNombre().equals(request.nombre()) && productoRepository.existsByNombre(request.nombre())) {
            throw new BusinessException("Ya existe un producto con el nombre: " + request.nombre());
        }

        productoExistente.setNombre(request.nombre());
        productoExistente.setDescripcion(request.descripcion());
        productoExistente.setPrecio(request.precio());
        productoExistente.setStock(request.stock());
        productoExistente.setCategoria(request.categoria());
        productoExistente.setImagenUrl(normalizeImageUrl(request.imagenUrl()));

        return productoRepository.save(productoExistente);
    }

    public void eliminarProducto(Long id) {
        Producto producto = obtenerProductoPorId(id);
        productoRepository.delete(producto);
    }

    private String normalizeImageUrl(String imagenUrl) {
        if (imagenUrl == null) {
            return null;
        }

        String normalized = imagenUrl.trim();
        return normalized.isBlank() ? null : normalized;
    }
}
