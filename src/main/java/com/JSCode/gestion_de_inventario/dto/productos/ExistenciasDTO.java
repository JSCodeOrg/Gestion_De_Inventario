package com.JSCode.gestion_de_inventario.dto.productos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "DTO utilizado para verificar si hay existencias suficientes de un producto")
public class ExistenciasDTO {

    @Schema(description = "ID del producto", example = "101")
    private long idProducto;

    @Schema(description = "Cantidad requerida del producto", example = "3")
    private int cantidad;
}
