package com.JSCode.gestion_de_inventario.dto.productos;

import java.math.BigDecimal;
import java.util.List;

import com.JSCode.gestion_de_inventario.models.Categoria;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ProductoCarruselDTO {
    private Long id;
    private List<String> imagenes;
    private Categoria categoria;
    private String descripcion;
    private String nombre;
    private BigDecimal precioCompra;
    
}
