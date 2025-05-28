package com.JSCode.gestion_de_inventario.controllers;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import com.JSCode.gestion_de_inventario.dto.Response.ApiResponse;
import com.JSCode.gestion_de_inventario.dto.productos.AgregarCantidadDTO;
import com.JSCode.gestion_de_inventario.dto.productos.AgregarProductNuevoDTO;
import com.JSCode.gestion_de_inventario.dto.productos.CategoriaDTO;
import com.JSCode.gestion_de_inventario.dto.productos.EditarProductoDTO;
import com.JSCode.gestion_de_inventario.dto.productos.ProductoCarruselDTO;
import com.JSCode.gestion_de_inventario.dto.productos.ProductoDTO;
import com.JSCode.gestion_de_inventario.dto.productos.ProductoResumenDTO;
import com.JSCode.gestion_de_inventario.services.ImageStorageService;
import com.JSCode.gestion_de_inventario.services.ProductoService;

@RestController
@RequestMapping("/productos")
public class ProductosController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ImageStorageService imgService;

    @GetMapping("/filtrar")
    public ResponseEntity<ApiResponse<List<ProductoResumenDTO>>> filtrarProductos(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) BigDecimal precioMin,
            @RequestParam(required = false) BigDecimal precioMax) {

        List<ProductoResumenDTO> productos = productoService.filtrarProductos(categoria, precioMin, precioMax);
        return ResponseEntity.ok(new ApiResponse<>("Productos filtrados con éxito", productos, false, 200));
    }

    @GetMapping("/buscar")
    public ResponseEntity<Page<ProductoResumenDTO>> buscarProductos(@RequestParam String texto,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductoResumenDTO> productos = productoService.buscarPorTexto(texto, pageable);
        return ResponseEntity.ok(productos);
    }


    @PreAuthorize("hasRole('administrador')")
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<ApiResponse<ProductoDTO>> actualizarProducto(@PathVariable Long id,
        @RequestPart("producto") EditarProductoDTO productoInfo, @RequestPart(value = "imagenes", required = false) List<MultipartFile> imagenesAñadidas) {

        ProductoDTO editedProduct = productoService.actualizarProducto(productoInfo, id, imagenesAñadidas);

        return ResponseEntity.ok(new ApiResponse<>("Producto actualizado con éxito", editedProduct, false, 200));

    }

    @PreAuthorize("hasRole('administrador')")
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<ApiResponse<ApiResponse<String>>> eliminarProducto(@PathVariable Long id) {
        ApiResponse<String> productoEliminar = productoService.productoEliminar(id);

        return ResponseEntity.ok(new ApiResponse<>(null, productoEliminar, false, 200));

    }

    @PostMapping("/agregar/{id}")
    public ResponseEntity<ApiResponse<String>> agregarUnidadesProducto(@PathVariable Long id,
            @RequestBody AgregarCantidadDTO cantidadDTO) {
        productoService.agregarUnidadesProducto(id, cantidadDTO);
        return ResponseEntity.ok(new ApiResponse<>("Producto agregado con éxito", false, 200));
    }

    @GetMapping("/ver/{id}")
    public ResponseEntity<ApiResponse<ProductoDTO>> verProducto(@PathVariable Long id) {
        ProductoDTO producto = productoService.verProducto(id);
        return ResponseEntity.ok(new ApiResponse<>("Producto encontrado con éxito", producto, false, 200));
    }

    @GetMapping("/obtener/categoria")
    public ResponseEntity<List<ProductoCarruselDTO>> obtenerPorCategoria(@RequestParam Long categoria_id) {
        List<ProductoCarruselDTO> productos = this.productoService.obtenerProductosCarrusel(categoria_id);
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/categorias")
    public ResponseEntity<ApiResponse<List<CategoriaDTO>>> obtenerCategorias() {
        List<CategoriaDTO> categorias = productoService.obtenerCategorias();
        return ResponseEntity.ok(new ApiResponse<>("Categorias obtenidas con éxito", categorias, false, 200));
    }

    @PreAuthorize("hasRole('administrador')")
    @PostMapping("/agregar/nuevoproducto")
    public ResponseEntity<ApiResponse<ProductoDTO>> agregarProductoNuevo(
            @RequestBody AgregarProductNuevoDTO productoDTO) {
        ProductoDTO productoAgregado = productoService.agregarProductoNuevo(productoDTO);
        return ResponseEntity.ok(new ApiResponse<>("Producto agregado con éxito", productoAgregado, false, 201));
    }
}
