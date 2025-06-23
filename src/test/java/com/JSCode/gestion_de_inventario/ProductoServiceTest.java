package com.JSCode.gestion_de_inventario;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.jscode.gestion_de_inventario.dto.productos.ProductoDTO;
import com.jscode.gestion_de_inventario.exceptions.ResourceNotFoundException;
import com.jscode.gestion_de_inventario.models.Categoria;
import com.jscode.gestion_de_inventario.models.Imagenes;
import com.jscode.gestion_de_inventario.models.Productos;
import com.jscode.gestion_de_inventario.repositories.CategoriaRepository;
import com.jscode.gestion_de_inventario.repositories.ProductoRepository;
import com.jscode.gestion_de_inventario.services.ProductoService;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductoServiceTest {
/* 
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
        */
}