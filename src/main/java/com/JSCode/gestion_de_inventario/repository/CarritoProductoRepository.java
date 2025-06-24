package com.JSCode.gestion_de_inventario.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.JSCode.gestion_de_inventario.model.Carrito;
import com.JSCode.gestion_de_inventario.model.CarritoProducto;

public interface CarritoProductoRepository
                extends JpaRepository<CarritoProducto, Long>, JpaSpecificationExecutor<CarritoProducto> {

        List<CarritoProducto> findAllByCarrito(Carrito carrito);

        Optional<CarritoProducto> findByCarritoAndProductoId(Carrito carrito, Long productoId);

        Optional<CarritoProducto> findByCarrito(Carrito carrito);

}