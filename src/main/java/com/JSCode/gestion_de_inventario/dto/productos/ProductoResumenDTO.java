package com.JSCode.gestion_de_inventario.dto.productos;

import java.math.BigDecimal;
import java.util.List;

import com.JSCode.gestion_de_inventario.dto.ImagesDTO;
import com.JSCode.gestion_de_inventario.models.Categoria;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Resumen de información del producto")
public class ProductoResumenDTO {
    @Schema(description = "ID del producto", example = "1")
    private Long id;
    @Schema(description = "Nombre del producto", example = "Laptop Lenovo")
    private String nombre;
    @Schema(description = "Descripción del producto", example = "Portátil Lenovo con 16GB RAM y SSD 512GB")
    private String descripcion;
    @Schema(description = "Categoría del producto")
    private Categoria categoria;
    @Schema(description = "Precio de compra del producto", example = "2499000.00")
    private BigDecimal precioCompra;
    @Schema(description = "Lista de imágenes asociadas al producto")
    private List<ImagesDTO> imagenes;
}