package com.JSCode.gestion_de_inventario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.JSCode.gestion_de_inventario.model.Imagenes;

public interface ImagenesRepository extends JpaRepository<Imagenes, Long>{
    List<Imagenes> findByProductoId(Long productoId);
    
}
