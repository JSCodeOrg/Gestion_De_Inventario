package com.JSCode.gestion_de_inventario.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.JSCode.gestion_de_inventario.models.Productos;


public interface ProductoRepository extends JpaRepository<Productos, Long>, JpaSpecificationExecutor<Productos> {
}
