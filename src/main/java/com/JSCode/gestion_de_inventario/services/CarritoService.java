package com.jscode.gestion_de_inventario.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jscode.gestion_de_inventario.dto.carrito.AgregarProductoDTO;
import com.jscode.gestion_de_inventario.dto.carrito.EditarCarritoDTO;
import com.jscode.gestion_de_inventario.dto.carrito.ObtenerCarritoDTO;
import com.jscode.gestion_de_inventario.dto.carrito.ProductoEnCarritoDTO;
import com.jscode.gestion_de_inventario.dto.response.ApiResponse;
import com.jscode.gestion_de_inventario.models.Carrito;
import com.jscode.gestion_de_inventario.models.CarritoProducto;
import com.jscode.gestion_de_inventario.models.Productos;
import com.jscode.gestion_de_inventario.repositories.CarritoProductoRepository;
import com.jscode.gestion_de_inventario.repositories.CarritoRepository;
import com.jscode.gestion_de_inventario.repositories.ProductoRepository;
import com.jscode.gestion_de_inventario.security.JwtUtil;

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

        String iduserextracted = this.jwtUtil.extractUsername(token);
        Long useridparsed = Long.parseLong(iduserextracted);

        // Voy a asumir que el usuario existe jkasj

        Productos product = this.productoRepository.findById(producto.getId_producto())
                .orElseThrow(() -> new NotFoundException("El producto solicitado no fué encontrado"));

        if (product.getCantidadDisponible() < producto.getCantidad()) {
            throw new BadRequestException("La cantidad solicitada excede la cantidad disponible");
        }

        Carrito carrito = carritoRepository.findByUserId(useridparsed)
                .orElseGet(() -> {
                    Carrito nuevocarrito = new Carrito();
                    nuevocarrito.setUserId(useridparsed);
                    return nuevocarrito;
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
                .orElseThrow(() -> new NotFoundException("No se ha encontrado un carrito asociado a este usuario."));

        List<CarritoProducto> productoscarrito = this.carritoProductoRepository.findAllByCarrito(carrito);

        if (productoscarrito.isEmpty()) {
            throw new NotFoundException("No se ha encontrado productos en el carrito.");
        }

        List<ProductoEnCarritoDTO> productosDTO = productoscarrito.stream().map(cp -> {
            Productos producto = productoRepository.findById(cp.getProductoId())
                    .orElseThrow(() -> new NotFoundException("Producto no encontrado con id: " + cp.getProductoId()));

            String imageUrl = producto.getImagenes().isEmpty() ? null : producto.getImagenes().get(0).getImageUrl();

            return new ProductoEnCarritoDTO(producto.getId(), producto.getNombre(), producto.getPrecioCompra(),
                    cp.getCantidad(), imageUrl);
        }).toList();

        ObtenerCarritoDTO carritoobtenido = new ObtenerCarritoDTO();
        carritoobtenido.setProductos(productosDTO);

        return carritoobtenido;

    }

    public ApiResponse<Integer> editarCarrito(String authToken, EditarCarritoDTO carritoEditado) {

        String iduser = jwtUtil.extractUsername(authToken);

        Long iduserlong = Long.parseLong(iduser);

        Carrito carritousuario = this.carritoRepository.findByUserId(iduserlong)
                .orElseThrow(() -> new NotFoundException("No se ha encontrado un carrito asociado al usuario"));

        List<ProductoEnCarritoDTO> productosEditados = carritoEditado.getProductosEditados();

        for (ProductoEnCarritoDTO producto : productosEditados) {
            Long productoid = producto.getId();

            Productos busquedaproducto = this.productoRepository.findById(productoid)
                    .orElseThrow(() -> new NotFoundException("No se ha encontrado el producto solicitado"));

            if (producto.getCantidad() > busquedaproducto.getCantidadDisponible()) {
                return new ApiResponse<Integer>(
                        "El producto \"" + busquedaproducto.getNombre()
                                + "\" excede el límite de existencias disponibles: ",
                        busquedaproducto.getCantidadDisponible(),
                        true,
                        400);
            }

            CarritoProducto carritoProducto = this.carritoProductoRepository
                    .findByCarritoAndProductoId(carritousuario, productoid)
                    .orElseThrow(() -> new NotFoundException("No se ha encontrado el producto en el carrito"));

            carritoProducto.setCantidad(producto.getCantidad());

            this.carritoProductoRepository.save(carritoProducto);

        }

        List<ProductoEnCarritoDTO> productoseliminados = carritoEditado.getProductosEliminados();

        if (productoseliminados.isEmpty()) {
            for (ProductoEnCarritoDTO productoaeliminar : productoseliminados) {

                Long productoid = productoaeliminar.getId();

                CarritoProducto carritoProducto = this.carritoProductoRepository
                        .findByCarritoAndProductoId(carritousuario, productoid)
                        .orElseThrow(() -> new NotFoundException(
                                "No se encontró el producto que desea elimninar en el carrito"));

                this.carritoProductoRepository.delete(carritoProducto);

            }

        }

        return new ApiResponse<Integer>("Carrito editado correctamente", false, 200);
    }
}