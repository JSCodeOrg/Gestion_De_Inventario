package com.JSCode.gestion_de_inventario.dto.carrito;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductoEnCarritoDTO {

    private String nombre;
    private Integer cantidad;
    private String imageUrl;

}
