package com.JSCode.gestion_de_inventario.dto.carrito;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Schema(description = "DTO para editar el carrito: productos modificados y eliminados")
public class EditarCarritoDTO {

    @Schema(
        description = "Lista de productos cuya cantidad fue modificada en el carrito",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private List<ProductoEnCarritoDTO> productosEditados;

    @Schema(
        description = "Lista de productos que se eliminaron del carrito",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private List<ProductoEnCarritoDTO> productosEliminados;

}
