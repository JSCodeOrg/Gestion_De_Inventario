package com.JSCode.gestion_de_inventario.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.JSCode.gestion_de_inventario.dto.Response.ApiResponse;
import com.JSCode.gestion_de_inventario.dto.carrito.AgregarProductoDTO;
import com.JSCode.gestion_de_inventario.dto.carrito.EditarCarritoDTO;
import com.JSCode.gestion_de_inventario.dto.carrito.ObtenerCarritoDTO;
import com.JSCode.gestion_de_inventario.dto.carrito.ProductoEnCarritoDTO;
import com.JSCode.gestion_de_inventario.models.Carrito;
import com.JSCode.gestion_de_inventario.models.CarritoProducto;
import com.JSCode.gestion_de_inventario.models.Productos;
import com.JSCode.gestion_de_inventario.repositories.CarritoProductoRepository;
import com.JSCode.gestion_de_inventario.repositories.CarritoRepository;
import com.JSCode.gestion_de_inventario.repositories.ProductoRepository;
import com.JSCode.gestion_de_inventario.security.JwtUtil;

import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El producto solicitado no fué encontrado"));

        if (product.getCantidadDisponible() < producto.getCantidad()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cantidad solicitada excede la cantidad disponible");
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

    public ObtenerCarritoDTO obtenerCarrito(String token) {

        String userId = jwtUtil.extractUsername(token);
        Long userIdLong = Long.parseLong(userId);

        Carrito carrito = this.carritoRepository.findByUserId(userIdLong)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado un carrito asociado a este usuario."));

        List<CarritoProducto> productosCarrito = this.carritoProductoRepository.findAllByCarrito(carrito);

        if (productosCarrito.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado productos en el carrito.");
        }

        List<ProductoEnCarritoDTO> productosDTO = productosCarrito.stream().map(cp -> {
            Productos producto = productoRepository.findById(cp.getProductoId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado con id: " + cp.getProductoId()));

            String imageUrl = producto.getImagenes().isEmpty() ? null : producto.getImagenes().get(0).getImageUrl();

            return new ProductoEnCarritoDTO(producto.getId(), producto.getNombre(), producto.getPrecioCompra(),
                    cp.getCantidad(), imageUrl);
        }).toList();

        ObtenerCarritoDTO carritoObtenido = new ObtenerCarritoDTO();
        carritoObtenido.setProductos(productosDTO);

        return carritoObtenido;

    }

    public ApiResponse<Integer> editarCarrito(String authToken, EditarCarritoDTO carritoEditado) {

        String user_id = jwtUtil.extractUsername(authToken);

        Long user_id_long = Long.parseLong(user_id);

        Carrito user_carrito = this.carritoRepository.findByUserId(user_id_long)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado un carrito asociado al usuario"));

        List<ProductoEnCarritoDTO> productosEditados = carritoEditado.getProductosEditados();

        for (ProductoEnCarritoDTO producto : productosEditados) {
            Long producto_id = producto.getId();

            Productos producto_busqueda = this.productoRepository.findById(producto_id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el producto solicitado"));

            if (producto.getCantidad() > producto_busqueda.getCantidadDisponible()) {
                return new ApiResponse<Integer>(
                        "El producto \"" + producto_busqueda.getNombre()
                                + "\" excede el límite de existencias disponibles: ",
                        producto_busqueda.getCantidadDisponible(),
                        true,
                        400);
            }

            CarritoProducto carritoProducto = this.carritoProductoRepository
                    .findByCarritoAndProductoId(user_carrito, producto_id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el producto en el carrito"));

            carritoProducto.setCantidad(producto.getCantidad());

            this.carritoProductoRepository.save(carritoProducto);

        }

        List<ProductoEnCarritoDTO> productosEliminados = carritoEditado.getProductosEliminados();

        if (productosEliminados.size() > 0) {
            for (ProductoEnCarritoDTO producto_eliminar : productosEliminados) {

                Long producto_id = producto_eliminar.getId();

                CarritoProducto carritoProducto = this.carritoProductoRepository
                        .findByCarritoAndProductoId(user_carrito, producto_id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró el producto que desea elimninar en el carrito"));

                this.carritoProductoRepository.delete(carritoProducto);

            }

        }

        return new ApiResponse<Integer>("Carrito editado correctamente", false, 200);
    }
}