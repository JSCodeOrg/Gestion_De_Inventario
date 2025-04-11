package com.JSCode.gestion_de_inventario.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.JSCode.gestion_de_inventario.dto.ProductoResumenDTO;
import com.JSCode.gestion_de_inventario.models.Productos;
import com.JSCode.gestion_de_inventario.repositories.ProductoRepository;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<ProductoResumenDTO> filtrarProductos(String nombre, String categoria, BigDecimal precioMin,
            BigDecimal precioMax) {
        Specification<Productos> spec = Specification.where(null);

        if (nombre != null && !nombre.isEmpty()) {
            spec = spec
                    .and((root, query, cb) -> cb.like(cb.lower(root.get("nombre")), "%" + nombre.toLowerCase() + "%"));
        }

        if (categoria != null && !categoria.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("categoria").get("nombreCategoria"), categoria));
        }

        if (precioMin != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("precioCompra"), precioMin));
        }

        if (precioMax != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("precioCompra"), precioMax));
        }

        List<Productos> productos = productoRepository.findAll(spec);

        return productos.stream().map(producto -> new ProductoResumenDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecioCompra(),
                producto.getImagenes().stream().map(imagen -> imagen.getImageUrl()).toList())).toList();
    }

    public List<ProductoResumenDTO> buscarPorTexto(String texto) {
        String keyword = "%" + texto.toLowerCase() + "%";

        List<Productos> productos = productoRepository.findAll((root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("nombre")), keyword),
                cb.like(cb.lower(root.get("descripcion")), keyword),
                cb.like(cb.lower(root.get("categoria").get("nombreCategoria")), keyword),
                cb.like(cb.lower(root.get("palabrasClave")), keyword)));

        return productos.stream().map(producto -> {
            List<String> imagenes = producto.getImagenes().stream()
                    .map(imagen -> imagen.getImageUrl())
                    .toList();

            return new ProductoResumenDTO(
                    producto.getId(),
                    producto.getNombre(),
                    producto.getDescripcion(),
                    producto.getPrecioCompra(),
                    imagenes);
        }).toList();
    }
}