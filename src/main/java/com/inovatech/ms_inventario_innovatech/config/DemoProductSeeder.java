package com.inovatech.ms_inventario_innovatech.config;

import com.inovatech.ms_inventario_innovatech.entity.Producto;
import com.inovatech.ms_inventario_innovatech.repository.ProductoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Datos demo solo para entorno local/desarrollo. No activar en produccion.
 */
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "innovatech.seed.demo-products.enabled", havingValue = "true")
public class DemoProductSeeder implements CommandLineRunner {

    private final ProductoRepository productoRepository;

    @Override
    public void run(String... args) {
        List<Producto> demoProducts = List.of(
                product("Notebook Lenovo ThinkPad E14", "Notebook empresarial de 14 pulgadas para productividad y trabajo hibrido.", 849990F, 12, "Computadores"),
                product("Monitor Samsung 24 pulgadas", "Monitor Full HD de 24 pulgadas con panel IPS para oficina y estudio.", 159990F, 20, "Monitores"),
                product("Teclado Mecanico Redragon", "Teclado mecanico retroiluminado con switches tactiles para gaming y productividad.", 49990F, 35, "Perifericos"),
                product("Mouse Logitech M650", "Mouse inalambrico ergonomico con desplazamiento silencioso y alta precision.", 39990F, 40, "Perifericos"),
                product("Audifonos HyperX Cloud", "Audifonos con microfono desmontable y sonido envolvente para reuniones y juegos.", 79990F, 18, "Audio"),
                product("Webcam Logitech C920", "Camara web Full HD 1080p con enfoque automatico para videollamadas.", 69990F, 16, "Accesorios"),
                product("Disco SSD Kingston 1TB", "Unidad SSD SATA de 1TB para mejorar velocidad de arranque y almacenamiento.", 89990F, 25, "Almacenamiento"),
                product("Router TP-Link Archer", "Router doble banda AC para cobertura estable en hogar u oficina pequena.", 59990F, 22, "Redes"),
                product("Silla Ergonomica Office Pro", "Silla ergonomica con soporte lumbar y ajuste de altura para jornada extendida.", 129990F, 10, "Mobiliario"),
                product("Hub USB-C Multipuerto", "Hub USB-C con HDMI, USB 3.0 y lector de tarjetas para notebooks modernos.", 44990F, 30, "Accesorios")
        );

        demoProducts.stream()
                .filter(product -> !productoRepository.existsByNombre(product.getNombre()))
                .forEach(productoRepository::save);
    }

    private Producto product(String nombre, String descripcion, Float precio, Integer stock, String categoria) {
        return new Producto(null, nombre, descripcion, precio, stock, categoria);
    }
}
