package com.jscode.gestion_de_inventario.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.jscode.gestion_de_inventario.models.Carrito;
import com.jscode.gestion_de_inventario.models.CarritoProducto;

public interface CarritoProductoRepository
                extends JpaRepository<CarritoProducto, Long>, JpaSpecificationExecutor<CarritoProducto> {

        List<CarritoProducto> findAllByCarrito(Carrito carrito);

        Optional<CarritoProducto> findByCarritoAndProductoId(Carrito carrito, Long productoId);

        Optional<CarritoProducto> findByCarrito(Carrito carrito);

}