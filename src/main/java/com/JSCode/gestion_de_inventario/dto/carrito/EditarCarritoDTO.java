package com.JSCode.gestion_de_inventario.dto.carrito;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class EditarCarritoDTO {

    private List<ProductoEnCarritoDTO> productosEditados;
    private List<ProductoEnCarritoDTO> productosEliminados;

}
