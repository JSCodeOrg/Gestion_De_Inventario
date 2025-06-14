package com.JSCode.gestion_de_inventario.dto.productos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExistenciasDTO {
    private long idProducto;
    private int cantidad;

}
