package com.JSCode.gestion_de_inventario.dto.productos;

import java.math.BigDecimal;
import java.util.List;

import com.JSCode.gestion_de_inventario.dto.ImagesDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductoDTO {
    private String nombre;
    private String descripcion;
    private Long categoriaId;
    private Integer cantidadDisponible;
    private BigDecimal precioCompra;
    private Integer stockMinimo;
    private List<ImagesDTO> urlsImagenes;
    private String palabrasClave;
    private List<ImagesDTO> newImages; 
    private List<ImagesDTO> deletedImages; 

}
