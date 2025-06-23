package com.jscode.gestion_de_inventario.dto.carrito;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductoEnCarritoDTO {

    private Long id;
    private String nombre;
    private BigDecimal precio;
    private Integer cantidad;
    private String imageUrl;

}
