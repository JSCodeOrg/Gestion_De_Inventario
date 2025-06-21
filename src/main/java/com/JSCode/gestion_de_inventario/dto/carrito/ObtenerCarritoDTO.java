package com.JSCode.gestion_de_inventario.dto.carrito;

import java.util.List;

import com.JSCode.gestion_de_inventario.models.CarritoProducto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Schema(description = "Respuesta al obtener el carrito de compras")
public class ObtenerCarritoDTO {

    @Schema(
        description = "Lista de productos actualmente en el carrito",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private List<ProductoEnCarritoDTO> productos;

}
