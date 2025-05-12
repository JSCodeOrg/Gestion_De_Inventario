package com.JSCode.gestion_de_inventario.dto.productos;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ProductoCarruselDTO {
    private Long producto_id;
    private String imageUrl;
    private String nombre;
    private BigDecimal precioCompra;
    
}
