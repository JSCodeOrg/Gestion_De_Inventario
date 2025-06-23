package com.jscode.gestion_de_inventario.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.jscode.gestion_de_inventario.models.Imagenes;

public interface ImagenesRepository extends JpaRepository<Imagenes, Long>{
    List<Imagenes> findByProductoId(Long productoId);
    
}
