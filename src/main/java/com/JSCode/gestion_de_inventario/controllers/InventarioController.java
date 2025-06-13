package com.JSCode.gestion_de_inventario.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.JSCode.gestion_de_inventario.dto.productos.ExistenciasDTO;
import com.JSCode.gestion_de_inventario.services.ProductoService;

@RestController
@RequestMapping("/existencias")
public class InventarioController {

    @Autowired
    private ProductoService productoService;

    @PostMapping
    public ResponseEntity<Boolean> verificarExistencias(List<ExistenciasDTO> productos) {

        Boolean isEnoughInventory = productoService.verificarExistencias(productos);

        if (!isEnoughInventory) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(false);
        }

        return ResponseEntity.ok(true);
    }

}
