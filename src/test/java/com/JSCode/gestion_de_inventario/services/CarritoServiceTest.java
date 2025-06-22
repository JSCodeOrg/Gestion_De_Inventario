package com.JSCode.gestion_de_inventario.services;

import com.JSCode.gestion_de_inventario.dto.carrito.AgregarProductoDTO;
import com.JSCode.gestion_de_inventario.models.Carrito;
import com.JSCode.gestion_de_inventario.models.Productos;
import com.JSCode.gestion_de_inventario.repositories.CarritoProductoRepository;
import com.JSCode.gestion_de_inventario.repositories.CarritoRepository;
import com.JSCode.gestion_de_inventario.repositories.ProductoRepository;
import com.JSCode.gestion_de_inventario.security.JwtUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.MockitoAnnotations;

public class CarritoServiceTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private CarritoRepository carritoRepository;

    @Mock
    private CarritoProductoRepository carritoProductoRepository;

    @InjectMocks
    private CarritoService carritoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddToCart_ProductoNoExiste_LanzaNotFound() {
        String token = "fake.jwt.token";
        Long userId = 1L;

        when(jwtUtil.extractUsername(token)).thenReturn(String.valueOf(userId));
        when(productoRepository.findById(anyLong())).thenReturn(Optional.empty());

        AgregarProductoDTO producto = new AgregarProductoDTO();
        producto.setId_producto(100L);
        producto.setCantidad(1);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> carritoService.addToCart(producto, token));

        assertEquals("404 NOT_FOUND \"El producto solicitado no fué encontrado\"", exception.getMessage());
    }

    @Test
    public void testAddToCart_CantidadExcedeStock_LanzaBadRequest() {
        String token = "fake.jwt.token";
        Long userId = 1L;

        when(jwtUtil.extractUsername(token)).thenReturn(String.valueOf(userId));

        Productos productoMock = new Productos();
        productoMock.setIdProducto(100L);
        productoMock.setCantidadDisponible(5);

        when(productoRepository.findById(100L)).thenReturn(Optional.of(productoMock));

        AgregarProductoDTO producto = new AgregarProductoDTO();
        producto.setId_producto(100L);
        producto.setCantidad(10); // excede stock

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> carritoService.addToCart(producto, token));

        assertEquals("400 BAD_REQUEST \"La cantidad solicitada excede la cantidad disponible\"", exception.getMessage());
    }

    @Test
    public void testAddToCart_ProductoValido_DevuelveDTO() {
        String token = "fake.jwt.token";
        Long userId = 1L;

        when(jwtUtil.extractUsername(token)).thenReturn(String.valueOf(userId));

        Productos productoMock = new Productos();
        productoMock.setIdProducto(100L);
        productoMock.setCantidadDisponible(20);

        when(productoRepository.findById(100L)).thenReturn(Optional.of(productoMock));

        Carrito carrito = new Carrito();
        carrito.setUserId(userId);
        carrito.setItems(new ArrayList<>());

        when(carritoRepository.findByUserId(userId)).thenReturn(Optional.of(carrito));
        when(carritoRepository.save(any(Carrito.class))).thenReturn(carrito);

        AgregarProductoDTO dto = new AgregarProductoDTO();
        dto.setId_producto(100L);
        dto.setCantidad(2);

        AgregarProductoDTO resultado = carritoService.addToCart(dto, token);

        assertNotNull(resultado);
        assertEquals(100L, resultado.getId_producto());
        assertEquals(2, resultado.getCantidad());
    }
}

