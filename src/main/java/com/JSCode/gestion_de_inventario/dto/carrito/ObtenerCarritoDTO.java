package com.JSCode.gestion_de_inventario.dto.carrito;

import java.util.List;

import com.JSCode.gestion_de_inventario.models.CarritoProducto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ObtenerCarritoDTO {
    private List<ProductoEnCarritoDTO> productos;

}
