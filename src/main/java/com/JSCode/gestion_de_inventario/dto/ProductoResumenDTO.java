package com.JSCode.gestion_de_inventario.dto;

import java.math.BigDecimal;
import java.util.List;

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
    private BigDecimal precioCompra;
    private List<String> imagenes;
}