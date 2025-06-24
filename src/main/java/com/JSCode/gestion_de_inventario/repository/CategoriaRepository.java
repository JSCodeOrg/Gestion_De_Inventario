package com.JSCode.gestion_de_inventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.JSCode.gestion_de_inventario.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    
}
