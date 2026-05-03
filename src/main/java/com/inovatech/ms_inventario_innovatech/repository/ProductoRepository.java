package com.inovatech.ms_inventario_innovatech.repository;

import com.inovatech.ms_inventario_innovatech.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    Optional<Producto> findByNombre(String nombre);
    
    boolean existsByNombre(String nombre);
}
