package com.JSCode.gestion_de_inventario.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.JSCode.gestion_de_inventario.dto.productos.ExistenciasDTO;
import com.JSCode.gestion_de_inventario.services.ProductoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/existencias")
@Tag(name = "Inventario", description = "Operaciones relacionadas con el inventario")
public class InventarioController {

    @Autowired
    private ProductoService productoService;


    @Operation(
    summary = "Verificar existencias de productos",
    description = "Verifica si hay suficiente inventario para una lista de productos antes de procesar una compra o reserva."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Existencias suficientes para todos los productos",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(value = "true")
            )
        ),
        @ApiResponse(
            responseCode = "409",
            description = "No hay suficiente inventario para uno o más productos",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(value = "false")
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Solicitud inválida (token no proporcionado o inválido)",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(value = """
                    {
                    "message": "Token no proporcionado o inválido",
                    "data": null,
                    "error": true,
                    "status": 400
                    }
                """)
            )
        )
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Lista de productos con sus cantidades solicitadas para verificar existencias",
        required = true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ExistenciasDTO.class),
            examples = @ExampleObject(value = """
                [
                {
                    "productoId": 101,
                    "cantidadSolicitada": 3
                },
                {
                    "productoId": 102,
                    "cantidadSolicitada": 1
                }
                ]
            """)
        )
    )
    @PostMapping
    public ResponseEntity<Boolean> verificarExistencias(@RequestBody List<ExistenciasDTO> productos, @RequestHeader("Authorization") String authToken) {

        Boolean isEnoughInventory = productoService.verificarExistencias(productos);

        if (!isEnoughInventory) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(false);
        }

        return ResponseEntity.ok(true);
    }

}
