package com.JSCode.gestion_de_inventario.dto.productos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "DTO para agregar unidades a un producto existente")
public class AgregarCantidadDTO {

    @Schema(description = "Cantidad de unidades a agregar", example = "10", required = true)
    private int cantidad;
}
