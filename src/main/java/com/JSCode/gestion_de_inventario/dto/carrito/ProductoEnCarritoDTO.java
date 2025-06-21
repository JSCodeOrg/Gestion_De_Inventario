package com.JSCode.gestion_de_inventario.dto.carrito;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Información de un producto dentro del carrito de compras")
public class ProductoEnCarritoDTO {

    @Schema(description = "ID único del producto", example = "101")
    private Long id;

    @Schema(description = "Nombre del producto", example = "Camiseta deportiva")
    private String nombre;

    @Schema(description = "Precio unitario del producto", example = "49900.00")
    private BigDecimal precio;

    @Schema(description = "Cantidad de unidades seleccionadas del producto", example = "2")
    private Integer cantidad;

    @Schema(description = "URL de la imagen principal del producto", example = "https://example.com/images/camiseta.png")
    private String imageUrl;
}
