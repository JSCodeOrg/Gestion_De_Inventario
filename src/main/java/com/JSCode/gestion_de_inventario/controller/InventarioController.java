package com.JSCode.gestion_de_inventario.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.JSCode.gestion_de_inventario.dto.productos.ActualizarStockDTO;
import com.JSCode.gestion_de_inventario.dto.productos.ExistenciasDTO;
import com.JSCode.gestion_de_inventario.service.ProductoService;

@RestController
@RequestMapping("/existencias")
public class InventarioController {

    @Autowired
    private ProductoService productoService;

    @PostMapping
    public ResponseEntity<Boolean> verificarExistencias(@RequestBody List<ExistenciasDTO> productos,
            @RequestHeader("Authorization") String authToken) {

        Boolean isEnoughInventory = productoService.verificarExistencias(productos);

        if (!isEnoughInventory) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(false);
        }

        return ResponseEntity.ok(true);
    }

    @PostMapping("/existencias/lote")
    public ResponseEntity<?> actualizarStockLote(@RequestBody List<ActualizarStockDTO> productos) {
        try {
            System.out.println("Hola");
            throw new RuntimeException("Simulación de error al actualizar stock");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error al actualizar stock: " + e.getMessage());
        }
    }
}
