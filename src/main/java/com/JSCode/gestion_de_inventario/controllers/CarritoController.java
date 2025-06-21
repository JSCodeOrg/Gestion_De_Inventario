package com.JSCode.gestion_de_inventario.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.JSCode.gestion_de_inventario.dto.Response.Response;
import com.JSCode.gestion_de_inventario.dto.carrito.AgregarProductoDTO;
import com.JSCode.gestion_de_inventario.dto.carrito.EditarCarritoDTO;
import com.JSCode.gestion_de_inventario.dto.carrito.ObtenerCarritoDTO;
import com.JSCode.gestion_de_inventario.security.JwtUtil;
import com.JSCode.gestion_de_inventario.services.CarritoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/carrito")
@Tag(name = "Carrito", description = "Operaciones relacionadas con el carrito")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private JwtUtil jwtUtil;

    @Operation(
    summary = "Agregar producto al carrito",
    description = "Permite al usuario autenticado agregar un producto al carrito de compras."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Producto añadido correctamente al carrito",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Response.class),
                examples = @ExampleObject(value = """
                    {
                    "message": "Carrito modificado",
                    "data": {
                        "productoId": 10,
                        "cantidad": 2
                    },
                    "error": false,
                    "status": 200
                    }
                """)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Token no válido o faltante",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(value = """
                    {
                    "message": "Token no válido",
                    "data": null,
                    "error": true,
                    "status": 400
                    }
                """)
            )
        )
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        required = true,
        description = "Datos del producto a agregar al carrito",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = AgregarProductoDTO.class),
            examples = @ExampleObject(value = """
                {
                "productoId": 10,
                "cantidad": 2
                }
            """)
        )
    )

    @PostMapping()
    public ResponseEntity<Response<AgregarProductoDTO>> agregarAlCarrito(
        @RequestBody AgregarProductoDTO producto,
        @RequestHeader("Authorization") 
        @Parameter(description = "Token JWT del usuario (formato Bearer)", required = true, example = "Bearer eyJhbGciOiJIUzI1NiJ9...")
        String authToken) {

        if (authToken == null || authToken.isBlank()) {
            return ResponseEntity
                    .badRequest()
                    .body(new Response<>("Token no válido", false, 0));
        }

        String token = authToken.substring(7);

        if (!jwtUtil.isTokenValid(token)) {
            return ResponseEntity
                    .badRequest()
                    .body(new Response<>("Token no válido", false, 0));
        }

        AgregarProductoDTO productoAñadido = carritoService.addToCart(producto, token);

        return ResponseEntity.ok(new Response<>("Carrito modificado", productoAñadido, false, 200));

    }

    // Endpoint para obtener el carrito del usuario autenticado

    @Operation(
    summary = "Obtener carrito de compras",
    description = "Retorna el contenido actual del carrito de compras del usuario autenticado."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Carrito obtenido correctamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Response.class),
                examples = @ExampleObject(value = """
                    {
                    "message": "Carrito obtenido correctamente",
                    "data": {
                        "productos": [
                        {
                            "productoId": 1,
                            "nombre": "Laptop Lenovo",
                            "cantidad": 2,
                            "precio": 2500.00
                        },
                        {
                            "productoId": 2,
                            "nombre": "Mouse inalámbrico",
                            "cantidad": 1,
                            "precio": 100.00
                        }
                        ],
                        "total": 5100.00
                    },
                    "error": false,
                    "status": 200
                    }
                """)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Token no válido o faltante",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(value = """
                    {
                    "message": "Token no válido",
                    "data": null,
                    "error": true,
                    "status": 400
                    }
                """)
            )
        )
    })
    @GetMapping()
    public ResponseEntity<Response<ObtenerCarritoDTO>> obtenerCarrito(
            @RequestHeader("Authorization")
            @Parameter(description = "Token JWT del usuario (formato Bearer)", required = true, example = "Bearer eyJhbGciOiJIUzI1NiJ9...")
            String authToken) {

        String token = authToken.substring(7);

        ObtenerCarritoDTO carritoObtenido = carritoService.obtenerCarrito(token);

        return ResponseEntity.ok(new Response<>("Carrito obtenido correctamente", carritoObtenido, false, 200));

    }

    // Endpoint para modificar el carrito del usuario autenticado

    @Operation(
    summary = "Modificar carrito de compras",
    description = "Permite modificar el contenido del carrito del usuario autenticado. Se pueden actualizar cantidades o eliminar productos."
)
@ApiResponses(value = {
    @ApiResponse(
        responseCode = "200",
        description = "Carrito modificado exitosamente",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = Response.class),
            examples = @ExampleObject(value = """
                {
                  "message": "Carrito actualizado correctamente",
                  "data": 3,
                  "error": false,
                  "status": 200
                }
            """)
        )
    ),
    @ApiResponse(
        responseCode = "400",
        description = "Token no válido o cuerpo de solicitud inválido",
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(value = """
                {
                  "message": "Token no válido",
                  "data": null,
                  "error": true,
                  "status": 400
                }
            """)
        )
    )
})
    @PutMapping()
    public ResponseEntity<Response<Integer>> modificarCarrito(
        @RequestHeader("Authorization") 
        @Parameter(description = "Token JWT del usuario (formato Bearer)", required = true, example = "Bearer eyJhbGciOiJIUzI1NiJ9...")
        String authToken, 
        @RequestBody 
         @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Objeto con las modificaciones al carrito",
        required = true,
        content = @Content(
            schema = @Schema(implementation = EditarCarritoDTO.class),
            examples = @ExampleObject(value = """
                {
                  "productos": [
                    { "productoId": 1, "nuevaCantidad": 2 },
                    { "productoId": 3, "nuevaCantidad": 0 }
                  ]
                }
            """)
        )
    )
        EditarCarritoDTO carritoEditado){

        String token = authToken.substring(7);

        Response<Integer> response = carritoService.editarCarrito(token, carritoEditado);

        return ResponseEntity.ok(response);
    }



}
