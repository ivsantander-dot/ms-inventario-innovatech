package com.inovatech.ms_inventario_innovatech.dto.response;

public record ProductoResponse(
        Long id,
        String nombre,
        String descripcion,
        Float precio,
        Integer stock,
        String categoria,
        String imagenUrl
) {
}
