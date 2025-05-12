package com.JSCode.gestion_de_inventario.controllers;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.JSCode.gestion_de_inventario.dto.Response.ApiResponse;
import com.JSCode.gestion_de_inventario.dto.productos.AgregarCantidadDTO;
import com.JSCode.gestion_de_inventario.dto.productos.ProductoCarruselDTO;
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

    @PreAuthorize("hasRole('administrador')")
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<ApiResponse<ApiResponse<String>>> eliminarProducto(@PathVariable Long id) {
        ApiResponse<String> productoEliminar = productoService.productoEliminar(id);

        return ResponseEntity.ok(new ApiResponse<>(null, productoEliminar, false, 200));

    }
    @PostMapping("/agregar/{id}")
    public ResponseEntity<ApiResponse<String>> agregarUnidadesProducto(@PathVariable Long id, @RequestBody AgregarCantidadDTO cantidadDTO) {
        productoService.agregarUnidadesProducto(id, cantidadDTO);
        return ResponseEntity.ok(new ApiResponse<>("Producto agregado con éxito", false, 200));
         }
    @GetMapping("/ver/{id}")
    public ResponseEntity<ApiResponse<ProductoDTO>> verProducto(@PathVariable Long id) {
        ProductoDTO producto = productoService.verProducto(id);
        return ResponseEntity.ok(new ApiResponse<>("Producto encontrado con éxito", producto, false, 200));   
    }  

    @PreAuthorize("hasRole('administrador')")
    @GetMapping("/obtener/categoria")
    public ResponseEntity<List<ProductoCarruselDTO>> obtenerPorCategoria(@RequestParam Long categoria_id){
        List<ProductoCarruselDTO> productos = this.productoService.obtenerProductosCarrusel(categoria_id);
        return ResponseEntity.ok(productos);
    }
}
