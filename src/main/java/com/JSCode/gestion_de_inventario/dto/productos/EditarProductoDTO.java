package com.JSCode.gestion_de_inventario.dto.productos;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Schema(description = "DTO para editar un producto existente")
public class EditarProductoDTO {
    @Schema(description = "Cantidad disponible del producto", example = "25")
    private Integer cantidadDisponible;

    @Schema(description = "ID de la categoría a la que pertenece el producto", example = "3")
    private Integer categoriaId;

    @Schema(description = "Descripción detallada del producto", example = "Monitor 27 pulgadas Full HD con HDMI y DisplayPort")
    private String descripcion;

    @Schema(description = "Lista de IDs de imágenes que se deben eliminar", example = "[1, 3]")
    private List<Long> imagenesEliminadas;

    @Schema(description = "Nombre del producto", example = "Monitor Samsung 27 pulgadas")
    private String nombre;

    @Schema(description = "Precio de compra del producto", example = "749900.00")
    private BigDecimal precioCompra;

    @Schema(description = "Stock mínimo permitido antes de generar alerta", example = "5")
    private Integer stockMinimo;

    @Schema(description = "ID del producto a editar", example = "12")
    private Integer producto_id;

    @Schema(description = "Palabras clave para mejorar la búsqueda del producto", example = "monitor, samsung, 27 pulgadas, FHD")
    private String palabrasClave;
}
