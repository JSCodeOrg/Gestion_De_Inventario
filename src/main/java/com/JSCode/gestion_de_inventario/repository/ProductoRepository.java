package com.JSCode.gestion_de_inventario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.JSCode.gestion_de_inventario.model.Categoria;
import com.JSCode.gestion_de_inventario.model.Productos;

public interface ProductoRepository extends JpaRepository<Productos, Long>, JpaSpecificationExecutor<Productos> {

    List<Productos> findTop15ByCategoriaOrderByIdAsc(Categoria categoria);


}
