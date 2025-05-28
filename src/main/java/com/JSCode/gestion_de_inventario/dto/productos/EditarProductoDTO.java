package com.JSCode.gestion_de_inventario.dto.productos;

import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EditarProductoDTO {
    private Integer cantidadDisponible;
    private Integer categoriaId;
    private String descripcion;
    private List<Long> imagenesEliminadas;
    private String nombre;
    private BigDecimal precioCompra;
    private Integer stockMinimo;
    private Integer producto_id;
    private String palabrasClave;
}
