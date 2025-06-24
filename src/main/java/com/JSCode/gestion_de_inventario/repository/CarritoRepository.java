package com.JSCode.gestion_de_inventario.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.JSCode.gestion_de_inventario.model.Carrito;

public interface CarritoRepository extends JpaRepository<Carrito, Long>, JpaSpecificationExecutor<Carrito> {
     Optional<Carrito> findByUserId(Long userId);

}
