package com.JSCode.gestion_de_inventario.dto.productos;

import java.math.BigDecimal;
import java.util.List;

import com.JSCode.gestion_de_inventario.dto.images.ImagesDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AgregarProductNuevoDTO {
    private String nombre;
    private String descripcion;
    private Integer cantidadDisponible;
    private BigDecimal precioCompra;
    private Integer stockMinimo;
    private Long categoriaId;
    private String palabrasClave;
    private List<ImagesDTO> imagenesUrls;
}