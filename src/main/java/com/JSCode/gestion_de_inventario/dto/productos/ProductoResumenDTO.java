package com.JSCode.gestion_de_inventario.dto.productos;

import java.math.BigDecimal;
import java.util.List;

import com.JSCode.gestion_de_inventario.dto.ImagesDTO;
import com.JSCode.gestion_de_inventario.models.Categoria;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductoResumenDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Categoria categoria;
    private BigDecimal precioCompra;
    private List<ImagesDTO> imagenes;
}