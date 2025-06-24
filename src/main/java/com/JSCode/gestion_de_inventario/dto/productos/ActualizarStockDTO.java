package com.JSCode.gestion_de_inventario.dto.productos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarStockDTO {
    private Long productoId;
    private Integer cantidad;
}