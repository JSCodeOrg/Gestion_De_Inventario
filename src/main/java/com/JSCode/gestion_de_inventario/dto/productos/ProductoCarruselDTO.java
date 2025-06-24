package com.JSCode.gestion_de_inventario.dto.productos;

import java.math.BigDecimal;
import java.util.List;

import com.JSCode.gestion_de_inventario.dto.images.ImagesDTO;
import com.JSCode.gestion_de_inventario.model.Categoria;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ProductoCarruselDTO {
    private Long id;
    private List<ImagesDTO> imagenes;
    private Categoria categoria;
    private String descripcion;
    private String nombre;
    private BigDecimal precioCompra;
    
}
