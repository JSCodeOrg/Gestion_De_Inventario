package com.JSCode.gestion_de_inventario.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.JSCode.gestion_de_inventario.models.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    
}
