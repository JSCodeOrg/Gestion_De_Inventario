package com.JSCode.gestion_de_inventario.dto.productos;

import java.math.BigDecimal;
import java.util.List;

import com.JSCode.gestion_de_inventario.dto.ImagesDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "DTO con información detallada de un producto")
public class ProductoDTO {

    @Schema(description = "Nombre del producto", example = "Teclado mecánico RGB")
    private String nombre;

    @Schema(description = "Descripción del producto", example = "Teclado mecánico con retroiluminación RGB y switches rojos")
    private String descripcion;

    @Schema(description = "ID de la categoría a la que pertenece el producto", example = "2")
    private Long categoriaId;

    @Schema(description = "Cantidad disponible del producto", example = "100")
    private Integer cantidadDisponible;

    @Schema(description = "Precio de compra del producto", example = "120000.00")
    private BigDecimal precioCompra;

    @Schema(description = "Stock mínimo requerido para el producto", example = "10")
    private Integer stockMinimo;

    @Schema(description = "Imágenes actuales del producto")
    private List<ImagesDTO> urlsImagenes;

    @Schema(description = "Palabras clave para búsqueda del producto", example = "teclado, RGB, mecánico")
    private String palabrasClave;

    @Schema(description = "Nuevas imágenes añadidas al producto")
    private List<ImagesDTO> newImages;

    @Schema(description = "Imágenes eliminadas del producto")
    private List<ImagesDTO> deletedImages;

    @Schema(description = "ID único del producto", example = "15")
    private Long producto_id;
}
