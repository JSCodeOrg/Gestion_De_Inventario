package com.JSCode.gestion_de_inventario;

import com.JSCode.gestion_de_inventario.dto.productos.ProductoDTO;
import com.JSCode.gestion_de_inventario.dto.productos.ProductoResumenDTO;
import com.JSCode.gestion_de_inventario.exceptions.ResourceNotFoundException;
import com.JSCode.gestion_de_inventario.models.Categoria;
import com.JSCode.gestion_de_inventario.models.Imagenes;
import com.JSCode.gestion_de_inventario.models.Productos;
import com.JSCode.gestion_de_inventario.repositories.CategoriaRepository;
import com.JSCode.gestion_de_inventario.repositories.ProductoRepository;
import com.JSCode.gestion_de_inventario.services.ProductoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.jpa.domain.Specification;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProductoServiceTest {

    private ProductoService productoService;
    private ProductoRepository productoRepository;
    private CategoriaRepository categoriaRepository;

    @BeforeEach
    void setUp() throws Exception {
        productoRepository = mock(ProductoRepository.class);
        categoriaRepository = mock(CategoriaRepository.class);
        productoService = new ProductoService();

        Field productoRepoField = ProductoService.class.getDeclaredField("productoRepository");
        productoRepoField.setAccessible(true);
        productoRepoField.set(productoService, productoRepository);

        Field categoriaRepoField = ProductoService.class.getDeclaredField("categoriaRepository");
        categoriaRepoField.setAccessible(true);
        categoriaRepoField.set(productoService, categoriaRepository);
    }

    @Test
    void testActualizarProducto_ok() {
        Long productoId = 1L;
        ProductoDTO dto = new ProductoDTO();
        dto.setNombre("Nuevo nombre");
        dto.setDescripcion("Descripción actualizada");
        dto.setCantidadDisponible(20);
        dto.setPrecioCompra(new BigDecimal("50.00"));
        dto.setStockMinimo(5);
        dto.setPalabrasClave("nuevas palabras");
        dto.setCategoriaId(2L);
        dto.setNewImages(List.of("img1.jpg"));
        dto.setDeletedImages(List.of("img2.jpg"));

        Categoria categoria = new Categoria();
        categoria.setId(2L);
        categoria.setNombreCategoria("Electrónica");

        Imagenes imagenExistente = new Imagenes();
        imagenExistente.setImageUrl("img2.jpg");

        Productos productoExistente = new Productos();
        productoExistente.setId(productoId);
        productoExistente.setNombre("Viejo nombre");
        productoExistente.setDescripcion("Vieja descripción");
        productoExistente.setCantidadDisponible(10);
        productoExistente.setPrecioCompra(new BigDecimal("30.00"));
        productoExistente.setStockMinimo(2);
        productoExistente.setPalabrasClave("viejas");
        productoExistente.setCategoria(categoria);
        productoExistente.setImagenes(new ArrayList<>(List.of(imagenExistente)));

        when(productoRepository.findById(productoId)).thenReturn(Optional.of(productoExistente));
        when(categoriaRepository.findById(2L)).thenReturn(Optional.of(categoria));
        when(productoRepository.save(Mockito.any(Productos.class))).thenAnswer(inv -> inv.getArgument(0));

        ProductoDTO actualizado = productoService.actualizarProducto(dto, productoId);

        assertEquals("Nuevo nombre", actualizado.getNombre());
        assertEquals("Descripción actualizada", actualizado.getDescripcion());
        assertEquals(1, actualizado.getUrlsImagenes().size());
        assertTrue(actualizado.getUrlsImagenes().contains("img1.jpg"));
    }

    @Test
    void testActualizarProducto_productoNoEncontrado() {
        Long productoId = 100L;
        ProductoDTO dto = new ProductoDTO();
        dto.setNombre("Fantasma");

        when(productoRepository.findById(productoId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> productoService.actualizarProducto(dto, productoId)
        );

        assertEquals("Producto no encontrado con ID: 100", exception.getMessage());
    }

    @Test
    void testActualizarProducto_categoriaNoEncontrada() {
        Long productoId = 1L;
        ProductoDTO dto = new ProductoDTO();
        dto.setCategoriaId(99L);

        Productos productoExistente = new Productos();
        productoExistente.setId(productoId);
        productoExistente.setImagenes(new ArrayList<>());

        when(productoRepository.findById(productoId)).thenReturn(Optional.of(productoExistente));
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> productoService.actualizarProducto(dto, productoId)
        );

        assertEquals("Categoría no encontrada con ID: 99", exception.getMessage());
    }
    @Test
    void testFiltrarProductos_porNombreYCategoria() {
    
        Productos producto = new Productos();
        producto.setId(1L);
        producto.setNombre("Laptop ASUS");
        producto.setDescripcion("Portátil de alto rendimiento");
        producto.setPrecioCompra(new BigDecimal("1500.00"));

        Categoria categoria = new Categoria();
        categoria.setId(2L);
        categoria.setNombreCategoria("Electrónica");
        producto.setCategoria(categoria);

        Imagenes imagen = new Imagenes();
        imagen.setImageUrl("laptop.jpg");
        imagen.setProducto(producto);
        producto.setImagenes(List.of(imagen));

        
        when(productoRepository.findAll(any(Specification.class))).thenReturn(List.of(producto));

        List<ProductoResumenDTO> resultado = productoService.filtrarProductos("laptop", "Electrónica", null, null);


        assertEquals(1, resultado.size());
        ProductoResumenDTO dto = resultado.get(0);
        assertEquals("Laptop ASUS", dto.getNombre());
        assertEquals("Portátil de alto rendimiento", dto.getDescripcion());
        assertEquals(new BigDecimal("1500.00"), dto.getPrecioCompra());
        assertEquals(List.of("laptop.jpg"), dto.getImagenes());
    }

    @Test
    void testFiltrarProductos_porPrecioMinimo() {
        
        Productos producto = new Productos();
        producto.setId(2L);
        producto.setNombre("Monitor Gamer");
        producto.setDescripcion("Monitor de 27 pulgadas");
        producto.setPrecioCompra(new BigDecimal("300.00"));

        Categoria categoria = new Categoria();
        categoria.setId(3L);
        categoria.setNombreCategoria("Periféricos");
        producto.setCategoria(categoria);

        Imagenes imagen = new Imagenes();
        imagen.setImageUrl("monitor.jpg");
        imagen.setProducto(producto);
        producto.setImagenes(List.of(imagen));

        when(productoRepository.findAll(any(Specification.class))).thenReturn(List.of(producto));

        
        List<ProductoResumenDTO> resultado = productoService.filtrarProductos(null, null, new BigDecimal("250.00"), null);

        
        assertEquals(1, resultado.size());
        ProductoResumenDTO dto = resultado.get(0);
        assertEquals("Monitor Gamer", dto.getNombre());
        assertEquals("Monitor de 27 pulgadas", dto.getDescripcion());
        assertEquals(new BigDecimal("300.00"), dto.getPrecioCompra());
        assertEquals(List.of("monitor.jpg"), dto.getImagenes());
}
    @Test
    void testFiltrarProductos_porPrecioMaximo() {
        
        Productos producto = new Productos();
        producto.setId(3L);
        producto.setNombre("Mouse inalámbrico");
        producto.setDescripcion("Mouse con conexión Bluetooth");
        producto.setPrecioCompra(new BigDecimal("25.00"));

        Categoria categoria = new Categoria();
        categoria.setId(4L);
        categoria.setNombreCategoria("Accesorios");
        producto.setCategoria(categoria);

        Imagenes imagen = new Imagenes();
        imagen.setImageUrl("mouse.jpg");
        imagen.setProducto(producto);
        producto.setImagenes(List.of(imagen));

        when(productoRepository.findAll(any(Specification.class))).thenReturn(List.of(producto));

       
        List<ProductoResumenDTO> resultado = productoService.filtrarProductos(null, null, null, new BigDecimal("30.00"));

        
        assertEquals(1, resultado.size());
        ProductoResumenDTO dto = resultado.get(0);
        assertEquals("Mouse inalámbrico", dto.getNombre());
        assertEquals("Mouse con conexión Bluetooth", dto.getDescripcion());
        assertEquals(new BigDecimal("25.00"), dto.getPrecioCompra());
        assertEquals(List.of("mouse.jpg"), dto.getImagenes());
    }

}