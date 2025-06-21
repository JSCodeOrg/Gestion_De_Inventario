package com.JSCode.gestion_de_inventario.dto.productos;

import java.math.BigDecimal;
import java.util.List;

import com.JSCode.gestion_de_inventario.dto.ImagesDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "DTO para agregar un nuevo producto al inventario")
public class AgregarProductNuevoDTO {

    @Schema(description = "Nombre del producto", example = "Audífonos inalámbricos")
    private String nombre;

    @Schema(description = "Descripción del producto", example = "Audífonos con cancelación de ruido y conexión Bluetooth 5.0")
    private String descripcion;

    @Schema(description = "Cantidad inicial disponible del producto", example = "25")
    private Integer cantidadDisponible;

    @Schema(description = "Precio de compra por unidad del producto", example = "120000.00")
    private BigDecimal precioCompra;

    @Schema(description = "Cantidad mínima en inventario antes de generar alerta de stock", example = "5")
    private Integer stockMinimo;

    @Schema(description = "ID de la categoría a la que pertenece el producto", example = "3")
    private Long categoriaId;

    @Schema(description = "Palabras clave separadas por coma para facilitar la búsqueda", example = "auriculares, bluetooth, sonido")
    private String palabrasClave;

    @Schema(description = "Lista de URLs de imágenes asociadas al producto")
    private List<ImagesDTO> imagenesUrls;
}