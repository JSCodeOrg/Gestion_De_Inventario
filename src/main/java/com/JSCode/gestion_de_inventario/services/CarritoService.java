package com.JSCode.gestion_de_inventario.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.JSCode.gestion_de_inventario.dto.carrito.AgregarProductoDTO;
import com.JSCode.gestion_de_inventario.models.Carrito;
import com.JSCode.gestion_de_inventario.models.CarritoProducto;
import com.JSCode.gestion_de_inventario.models.Productos;
import com.JSCode.gestion_de_inventario.repositories.CarritoProductoRepository;
import com.JSCode.gestion_de_inventario.repositories.CarritoRepository;
import com.JSCode.gestion_de_inventario.repositories.ProductoRepository;
import com.JSCode.gestion_de_inventario.security.JwtUtil;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

@Service
public class CarritoService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private CarritoProductoRepository carritoProductoRepository;

    public AgregarProductoDTO addToCart(AgregarProductoDTO producto, String token) {

        String user_id_extracted = this.jwtUtil.extractUsername(token);
        Long user_id_parsed = Long.parseLong(user_id_extracted);

        // Voy a asumir que el usuario existe jkasj

        Productos product = this.productoRepository.findById(producto.getId_producto())
                .orElseThrow(() -> new NotFoundException("El producto solicitado no fué encontrado"));

        if (product.getCantidadDisponible() < producto.getCantidad()) {
            throw new BadRequestException("La cantidad solicitada excede la cantidad disponible");
        }

        Carrito carrito = carritoRepository.findByUserId(user_id_parsed)
                .orElseGet(() -> {
                    Carrito nuevoCarrito = new Carrito();
                    nuevoCarrito.setUserId(user_id_parsed);
                    return nuevoCarrito;
                });

        Optional<CarritoProducto> productoExistente = carrito.getItems().stream()
                .filter(item -> item.getProductoId().equals(producto.getId_producto()))
                .findFirst();

        if (productoExistente.isPresent()) {
            CarritoProducto existente = productoExistente.get();
            existente.setCantidad(existente.getCantidad() + producto.getCantidad());
        } else {
            CarritoProducto nuevoItem = new CarritoProducto();
            nuevoItem.setProductoId(producto.getId_producto());
            nuevoItem.setCantidad(producto.getCantidad());
            nuevoItem.setCarrito(carrito);
            carrito.getItems().add(nuevoItem);
        }

        carritoRepository.save(carrito);

        return producto;

    }

}
