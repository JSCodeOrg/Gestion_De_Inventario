package com.JSCode.gestion_de_inventario.dto.carrito;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "DTO para agregar un producto al carrito de compras")
public class AgregarProductoDTO {

    @Schema(
        description = "ID del producto que se desea agregar",
        example = "101"
    )
    private Long id_producto;

    @Schema(
        description = "Cantidad del producto a agregar",
        example = "2"
    )
    private Integer cantidad;

}
