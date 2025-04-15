package com.JSCode.gestion_de_inventario.controllers;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.JSCode.gestion_de_inventario.dto.Response.ApiResponse;
import com.JSCode.gestion_de_inventario.dto.productos.ProductoDTO;
import com.JSCode.gestion_de_inventario.dto.productos.ProductoResumenDTO;
import com.JSCode.gestion_de_inventario.services.ProductoService;

@RestController
@RequestMapping("/api/productos")
public class ProductosController {

    @Autowired
    private ProductoService productoService;

    @GetMapping("/filtrar")
    public ResponseEntity<ApiResponse<List<ProductoResumenDTO>>> filtrarProductos(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) BigDecimal precioMin,
            @RequestParam(required = false) BigDecimal precioMax) {

        List<ProductoResumenDTO> productos = productoService.filtrarProductos(nombre, categoria, precioMin, precioMax);
        return ResponseEntity.ok(new ApiResponse<>("Productos filtrados con éxito", productos, false, 200));
    }

    @GetMapping("/buscar")
    public ResponseEntity<ApiResponse<List<ProductoResumenDTO>>> buscarProductos(@RequestParam String texto) {
        List<ProductoResumenDTO> productos = productoService.buscarPorTexto(texto);
        return ResponseEntity.ok(new ApiResponse<>("Productos encontrados con éxito", productos, false, 200));
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<ApiResponse<ProductoDTO>> actualizarProducto(@PathVariable Long id,
            @RequestBody ProductoDTO productoInfo) {

        ProductoDTO productoActualizado = productoService.actualizarProducto(productoInfo, id);

        return ResponseEntity.ok(new ApiResponse<>("Producto actualizado con éxito", productoActualizado, false, 200));

    }


}
