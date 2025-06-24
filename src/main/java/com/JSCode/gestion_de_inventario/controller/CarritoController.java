package com.JSCode.gestion_de_inventario.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.JSCode.gestion_de_inventario.dto.carrito.AgregarProductoDTO;
import com.JSCode.gestion_de_inventario.dto.carrito.EditarCarritoDTO;
import com.JSCode.gestion_de_inventario.dto.carrito.ObtenerCarritoDTO;
import com.JSCode.gestion_de_inventario.dto.response.ApiResponse;
import com.JSCode.gestion_de_inventario.security.JwtUtil;
import com.JSCode.gestion_de_inventario.service.CarritoService;

@RestController
@RequestMapping("/carrito")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping()
    public ResponseEntity<ApiResponse<AgregarProductoDTO>> agregarAlCarrito(@RequestBody AgregarProductoDTO producto,
            @RequestHeader("Authorization") String authToken) {

        if (authToken == null || authToken.isBlank()) {
            return ResponseEntity
                    .badRequest()
                    .body(new ApiResponse<>("Token no válido", false, 0));
        }

        String token = authToken.substring(7);

        if (!jwtUtil.isTokenValid(token)) {
            return ResponseEntity
                    .badRequest()
                    .body(new ApiResponse<>("Token no válido", false, 0));
        }

        AgregarProductoDTO productoAñadido = carritoService.addToCart(producto, token);

        return ResponseEntity.ok(new ApiResponse<>("Carrito modificado", productoAñadido, false, 200));

    }

    @GetMapping()
    public ResponseEntity<ApiResponse<ObtenerCarritoDTO>> obtenerCarrito(
            @RequestHeader("Authorization") String authToken) {

        String token = authToken.substring(7);

        ObtenerCarritoDTO carritoObtenido = carritoService.obtenerCarrito(token);

        return ResponseEntity.ok(new ApiResponse<>("Carrito obtenido correctamente", carritoObtenido, false, 200));

    }

    @PutMapping()
    public ResponseEntity<ApiResponse<Integer>> modificarCarrito(@RequestHeader("Authorization") String authToken, @RequestBody EditarCarritoDTO carritoEditado){

        System.out.println(carritoEditado);

        String token = authToken.substring(7);

        ApiResponse<Integer> response = carritoService.editarCarrito(token, carritoEditado);

        return ResponseEntity.ok(response);
    }



}
