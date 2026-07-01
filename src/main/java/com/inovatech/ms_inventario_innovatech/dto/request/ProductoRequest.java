package com.inovatech.ms_inventario_innovatech.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ProductoRequest(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
        String nombre,
        @NotBlank(message = "La descripcion es obligatoria")
        @Size(max = 500, message = "La descripcion no puede exceder 500 caracteres")
        String descripcion,
        @NotNull(message = "El precio es obligatorio")
        @DecimalMin(value = "0.0", inclusive = true, message = "El precio no puede ser negativo")
        Float precio,
        @NotNull(message = "El stock es obligatorio")
        @Min(value = 0, message = "El stock no puede ser negativo")
        Integer stock,
        @NotBlank(message = "La categoria es obligatoria")
        @Size(max = 50, message = "La categoria no puede exceder 50 caracteres")
        String categoria,
        @Size(max = 1000, message = "La URL de imagen no puede exceder 1000 caracteres")
        @Pattern(
                regexp = "^(|https?://.+)$",
                message = "La imagen debe ser una URL http o https valida"
        )
        String imagenUrl
) {
}
