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
                product("Notebook Lenovo ThinkPad E14", "Notebook empresarial de 14 pulgadas para productividad y trabajo hibrido.", 849990F, 12, "Computadores", "https://images.unsplash.com/photo-1496181133206-80ce9b88a853?auto=format&fit=crop&w=1200&q=80"),
                product("Monitor Samsung 24 pulgadas", "Monitor Full HD de 24 pulgadas con panel IPS para oficina y estudio.", 159990F, 20, "Monitores", "https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?auto=format&fit=crop&w=1200&q=80"),
                product("Teclado Mecanico Redragon", "Teclado mecanico retroiluminado con switches tactiles para gaming y productividad.", 49990F, 35, "Perifericos", "https://images.unsplash.com/photo-1511467687858-23d96c32e4ae?auto=format&fit=crop&w=1200&q=80"),
                product("Mouse Logitech M650", "Mouse inalambrico ergonomico con desplazamiento silencioso y alta precision.", 39990F, 40, "Perifericos", "https://images.unsplash.com/photo-1527814050087-3793815479db?auto=format&fit=crop&w=1200&q=80"),
                product("Audifonos HyperX Cloud", "Audifonos con microfono desmontable y sonido envolvente para reuniones y juegos.", 79990F, 18, "Audio", "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?auto=format&fit=crop&w=1200&q=80"),
                product("Webcam Logitech C920", "Camara web Full HD 1080p con enfoque automatico para videollamadas.", 69990F, 16, "Accesorios", "https://images.unsplash.com/photo-1587825140708-dfaf72ae4b04?auto=format&fit=crop&w=1200&q=80"),
                product("Disco SSD Kingston 1TB", "Unidad SSD SATA de 1TB para mejorar velocidad de arranque y almacenamiento.", 89990F, 25, "Almacenamiento", "https://images.unsplash.com/photo-1591488320449-011701bb6704?auto=format&fit=crop&w=1200&q=80"),
                product("Router TP-Link Archer", "Router doble banda AC para cobertura estable en hogar u oficina pequena.", 59990F, 22, "Redes", "https://images.unsplash.com/photo-1647427060118-4911c9821b82?auto=format&fit=crop&w=1200&q=80"),
                product("Silla Ergonomica Office Pro", "Silla ergonomica con soporte lumbar y ajuste de altura para jornada extendida.", 129990F, 10, "Mobiliario", "https://images.unsplash.com/photo-1505843513577-22bb7d21e455?auto=format&fit=crop&w=1200&q=80"),
                product("Hub USB-C Multipuerto", "Hub USB-C con HDMI, USB 3.0 y lector de tarjetas para notebooks modernos.", 44990F, 30, "Accesorios", "https://images.unsplash.com/photo-1625842268584-8f3296236761?auto=format&fit=crop&w=1200&q=80")
        );

        demoProducts.forEach(this::createOrUpdateByName);
    }

    private void createOrUpdateByName(Producto source) {
        productoRepository.findByNombre(source.getNombre())
                .ifPresentOrElse(existing -> {
                    existing.setDescripcion(source.getDescripcion());
                    existing.setPrecio(source.getPrecio());
                    existing.setStock(source.getStock());
                    existing.setCategoria(source.getCategoria());
                    existing.setImagenUrl(source.getImagenUrl());
                    productoRepository.save(existing);
                }, () -> productoRepository.save(source));
    }

    private Producto product(String nombre, String descripcion, Float precio, Integer stock, String categoria, String imagenUrl) {
        return new Producto(null, nombre, descripcion, precio, stock, categoria, imagenUrl);
    }
}
