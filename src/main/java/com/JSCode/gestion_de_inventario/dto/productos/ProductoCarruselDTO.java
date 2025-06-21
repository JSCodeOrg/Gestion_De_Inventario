package com.JSCode.gestion_de_inventario.dto.productos;

import java.math.BigDecimal;
import java.util.List;

import com.JSCode.gestion_de_inventario.dto.ImagesDTO;
import com.JSCode.gestion_de_inventario.models.Categoria;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Schema(description = "DTO que representa un producto mostrado en el carrusel de categorías")
public class ProductoCarruselDTO {

    @Schema(description = "ID del producto", example = "5")
    private Long id;

    @Schema(description = "Lista de imágenes del producto")
    private List<ImagesDTO> imagenes;

    @Schema(description = "Categoría a la que pertenece el producto")
    private Categoria categoria;

    @Schema(description = "Descripción breve del producto", example = "Mouse inalámbrico con sensor óptico")
    private String descripcion;

    @Schema(description = "Nombre del producto", example = "Mouse Logitech M170")
    private String nombre;

    @Schema(description = "Precio de compra del producto", example = "75000.00")
    private BigDecimal precioCompra;
}
