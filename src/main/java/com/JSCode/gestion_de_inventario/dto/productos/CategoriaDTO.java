package com.JSCode.gestion_de_inventario.dto.productos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Categoría del producto")
public class CategoriaDTO {
    @Schema(description = "ID de la categoría", example = "3")
    private Long id;
    @Schema(description = "Nombre de la categoría", example = "Tecnología")
    private String nombreCategoria;
}