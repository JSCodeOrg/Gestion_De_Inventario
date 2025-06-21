package com.JSCode.gestion_de_inventario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Información de una imagen del producto")
public class ImagesDTO {
    @Schema(description = "URL de la imagen", example = "https://miapp.com/images/producto1.png")
    private String url;
    @Schema(description = "ID de la imagen", example = "15")
    private Long id;
}
