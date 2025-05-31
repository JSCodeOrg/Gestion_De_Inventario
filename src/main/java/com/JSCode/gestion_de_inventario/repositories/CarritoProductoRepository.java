package com.JSCode.gestion_de_inventario.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.JSCode.gestion_de_inventario.models.CarritoProducto;

public interface CarritoProductoRepository
        extends JpaRepository<CarritoProducto, Long>, JpaSpecificationExecutor<CarritoProducto> {

            
}