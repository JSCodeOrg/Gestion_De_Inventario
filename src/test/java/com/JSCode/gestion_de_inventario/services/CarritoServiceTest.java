package com.JSCode.gestion_de_inventario.services;

import com.JSCode.gestion_de_inventario.dto.carrito.AgregarProductoDTO;
import com.JSCode.gestion_de_inventario.model.Carrito;
import com.JSCode.gestion_de_inventario.model.Productos;
import com.JSCode.gestion_de_inventario.repository.CarritoProductoRepository;
import com.JSCode.gestion_de_inventario.repository.CarritoRepository;
import com.JSCode.gestion_de_inventario.repository.ProductoRepository;
import com.JSCode.gestion_de_inventario.security.JwtUtil;
import com.JSCode.gestion_de_inventario.service.CarritoService;

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