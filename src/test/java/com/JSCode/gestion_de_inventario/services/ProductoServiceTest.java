package com.JSCode.gestion_de_inventario.services;

import com.JSCode.gestion_de_inventario.dto.ImagesDTO;
import com.JSCode.gestion_de_inventario.dto.Response.ApiResponse;
import com.JSCode.gestion_de_inventario.dto.productos.AgregarCantidadDTO;
import com.JSCode.gestion_de_inventario.dto.productos.ProductoDTO;
import com.JSCode.gestion_de_inventario.dto.productos.ExistenciasDTO;
import com.JSCode.gestion_de_inventario.exceptions.ResourceNotFoundException;
import com.JSCode.gestion_de_inventario.models.Categoria;
import com.JSCode.gestion_de_inventario.models.Imagenes;
import com.JSCode.gestion_de_inventario.models.Productos;
import com.JSCode.gestion_de_inventario.repositories.CategoriaRepository;
import com.JSCode.gestion_de_inventario.repositories.ImagenesRepository;
import com.JSCode.gestion_de_inventario.repositories.ProductoRepository;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductoServiceTest {

    @InjectMocks
    private ProductoService productoService;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;
    @Mock
    private ImagenesRepository imagenesRepository;

    @Mock
    private ImageStorageService imgService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void productoEliminar_ProductoExiste_Eliminado() {
        Long id = 1L;
        Productos producto = new Productos();
        when(productoRepository.findById(id)).thenReturn(Optional.of(producto));

        ApiResponse<String> response = productoService.productoEliminar(id);

        assertEquals("Producto Eliminado con Exito", response.getMessage());
        verify(productoRepository).deleteById(id);
    }

    @Test
    void verProducto_ProductoExiste_DevuelveDTO() {
        Long id = 1L;
        Productos producto = new Productos();
        producto.setId(id);
        producto.setNombre("Producto 1");
        producto.setDescripcion("Desc");
        producto.setCantidadDisponible(5);
        producto.setPrecioCompra(BigDecimal.TEN);
        producto.setStockMinimo(1);
        producto.setPalabrasClave("clave");
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        producto.setCategoria(categoria);
        producto.setImagenes(Collections.emptyList());
        when(productoRepository.findById(id)).thenReturn(Optional.of(producto));

        ProductoDTO dto = productoService.verProducto(id);

        assertEquals("Producto 1", dto.getNombre());
        assertEquals(1L, dto.getCategoriaId());
    }

    @Test
    void agregarUnidadesProducto_ProductoExiste_ActualizaCantidad() {
        Long id = 1L;
        AgregarCantidadDTO dto = new AgregarCantidadDTO();
        dto.setCantidad(10);

        Productos producto = new Productos();
        producto.setCantidadDisponible(5);

        when(productoRepository.findById(id)).thenReturn(Optional.of(producto));

        ApiResponse<String> response = productoService.agregarUnidadesProducto(id, dto);

        assertEquals("Unidades agregadas con éxito", response.getMessage());
        verify(productoRepository).save(producto);
        assertEquals(15, producto.getCantidadDisponible());
    }
    @Test
    void filtrarProductos_FiltraPorCategoriaYPrecio() {
        String categoria = "Electronica";
        BigDecimal precioMin = new BigDecimal("100.00");
        BigDecimal precioMax = new BigDecimal("500.00");

        Productos p = new Productos();
        p.setId(1L);
        p.setNombre("TV");
        p.setDescripcion("LED");
        p.setPrecioCompra(new BigDecimal("300.00"));
        Categoria cat = new Categoria();
        cat.setNombreCategoria(categoria);
        p.setCategoria(cat);
        p.setImagenes(Collections.emptyList());

        when(productoRepository.findAll(any(Specification.class))).thenReturn(List.of(p));

        var resultado = productoService.filtrarProductos(categoria, precioMin, precioMax);

        assertFalse(resultado.isEmpty());
        assertEquals("TV", resultado.get(0).getNombre());
    }

    @Test
    void verificarExistencias_ProductoSinStock_RetornaFalse() {
        Long id = 1L;
        ExistenciasDTO dto = new ExistenciasDTO();
        dto.setIdProducto(id);
        dto.setCantidad(10);

        Productos producto = new Productos();
        producto.setCantidadDisponible(5);

        when(productoRepository.findById(id)).thenReturn(Optional.of(producto));

        boolean result = productoService.verificarExistencias(List.of(dto));

        assertFalse(result);
    }
    @Test
    void verificarExistencias_ProductoConStock_RetornaTrue() {
        Long id = 1L;
        ExistenciasDTO dto = new ExistenciasDTO();
        dto.setIdProducto(id);
        dto.setCantidad(5);

        Productos producto = new Productos();
        producto.setCantidadDisponible(10);

        when(productoRepository.findById(id)).thenReturn(Optional.of(producto));

        boolean result = productoService.verificarExistencias(List.of(dto));

        assertTrue(result);
    }
}