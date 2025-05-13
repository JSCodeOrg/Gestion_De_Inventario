package com.JSCode.gestion_de_inventario.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.JSCode.gestion_de_inventario.dto.Response.ApiResponse;
import com.JSCode.gestion_de_inventario.dto.productos.AgregarCantidadDTO;
import com.JSCode.gestion_de_inventario.dto.productos.CategoriaDTO;
import com.JSCode.gestion_de_inventario.dto.productos.ProductoCarruselDTO;
import com.JSCode.gestion_de_inventario.dto.productos.ProductoDTO;
import com.JSCode.gestion_de_inventario.dto.productos.ProductoResumenDTO;
import com.JSCode.gestion_de_inventario.exceptions.ResourceNotFoundException;
import com.JSCode.gestion_de_inventario.models.Categoria;
import com.JSCode.gestion_de_inventario.models.Imagenes;
import com.JSCode.gestion_de_inventario.models.Productos;
import com.JSCode.gestion_de_inventario.repositories.CategoriaRepository;
import com.JSCode.gestion_de_inventario.repositories.ProductoRepository;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

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

    public ApiResponse<String> productoEliminar(Long id) {
        productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));
        productoRepository.deleteById(id);
        return new ApiResponse<String>("Producto Eliminado con Exito", false, 200);
    }

    public ProductoDTO actualizarProducto(ProductoDTO productoDTO, Long id) {
        Productos producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));

        if (productoDTO.getNombre() != null) {
            producto.setNombre(productoDTO.getNombre());
        }
        if (productoDTO.getDescripcion() != null) {
            producto.setDescripcion(productoDTO.getDescripcion());
        }
        if (productoDTO.getCantidadDisponible() != null) {
            producto.setCantidadDisponible(productoDTO.getCantidadDisponible());
        }
        if (productoDTO.getPrecioCompra() != null) {
            producto.setPrecioCompra(productoDTO.getPrecioCompra());
        }
        if (productoDTO.getStockMinimo() != null) {
            producto.setStockMinimo(productoDTO.getStockMinimo());
        }
        if (productoDTO.getPalabrasClave() != null) {
            producto.setPalabrasClave(productoDTO.getPalabrasClave());
        }

        if (productoDTO.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(productoDTO.getCategoriaId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Categoría no encontrada con ID: " + productoDTO.getCategoriaId()));
            producto.setCategoria(categoria);
        }

        if (productoDTO.getNewImages() != null) {
            List<String> imagenesExistentes = producto.getImagenes().stream()
                    .map(Imagenes::getImageUrl)
                    .collect(Collectors.toList());

            for (String url : productoDTO.getNewImages()) {
                if (!imagenesExistentes.contains(url)) {
                    Imagenes nuevaImagen = new Imagenes();
                    nuevaImagen.setImageUrl(url);
                    nuevaImagen.setProducto(producto);
                    producto.getImagenes().add(nuevaImagen);
                }
            }
        }

        if (productoDTO.getDeletedImages() != null) {
            producto.getImagenes().removeIf(imagen -> productoDTO.getDeletedImages().contains(imagen.getImageUrl()));
        }

        Productos productoGuardado = productoRepository.save(producto);

        ProductoDTO dto = new ProductoDTO();
        dto.setNombre(productoGuardado.getNombre());
        dto.setDescripcion(productoGuardado.getDescripcion());
        dto.setCantidadDisponible(productoGuardado.getCantidadDisponible());
        dto.setPrecioCompra(productoGuardado.getPrecioCompra());
        dto.setStockMinimo(productoGuardado.getStockMinimo());
        dto.setPalabrasClave(productoGuardado.getPalabrasClave());
        dto.setCategoriaId(productoGuardado.getCategoria().getId());

        List<String> urlsImagenes = productoGuardado.getImagenes().stream()
                .map(Imagenes::getImageUrl)
                .collect(Collectors.toList());
        dto.setUrlsImagenes(urlsImagenes);

        return dto;
    }

    public ApiResponse<String> agregarUnidadesProducto(Long id, AgregarCantidadDTO cantidadDTO) {

        Productos producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));
        int nuevaCantidad = producto.getCantidadDisponible() + cantidadDTO.getCantidad();
        producto.setCantidadDisponible(nuevaCantidad);
        productoRepository.save(producto);
        return new ApiResponse<>("Unidades agregadas con éxito", false, 200);
    }

    public ProductoDTO verProducto(Long id) {
        Productos producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));
        ProductoDTO dto = new ProductoDTO();
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setCantidadDisponible(producto.getCantidadDisponible());
        dto.setPrecioCompra(producto.getPrecioCompra());
        dto.setStockMinimo(producto.getStockMinimo());
        dto.setPalabrasClave(producto.getPalabrasClave());
        dto.setCategoriaId(producto.getCategoria().getId());

        List<String> urlsImagenes = producto.getImagenes().stream()
                .map(Imagenes::getImageUrl)
                .collect(Collectors.toList());
        dto.setUrlsImagenes(urlsImagenes);

        return dto;
    }

    public List<ProductoCarruselDTO> obtenerProductosCarrusel(Long categoria_id) {

        Categoria categoria = this.categoriaRepository.findById(categoria_id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + categoria_id));

        List<Productos> productos = this.productoRepository.findTop15ByCategoriaOrderByIdAsc(categoria);

        return productos.stream()
                .map(producto -> {
                    ProductoCarruselDTO dto = new ProductoCarruselDTO();
                    dto.setProducto_id(producto.getId());

                    if (!producto.getImagenes().isEmpty()) {
                        dto.setImageUrl(producto.getImagenes().get(0).getImageUrl());
                    } else {
                        dto.setImageUrl(null);
                    }

                    dto.setNombre(producto.getNombre());
                    dto.setPrecioCompra(producto.getPrecioCompra());

                    return dto;
                })
                .collect(Collectors.toList());
    }
    public List<CategoriaDTO> obtenerCategorias() {
        List<Categoria> categorias = categoriaRepository.findAll();
        return categorias.stream().map(categoria -> {
            CategoriaDTO dto = new CategoriaDTO();
            dto.setId(categoria.getId());
            dto.setNombreCategoria(categoria.getNombreCategoria());
            return dto;
        }).collect(Collectors.toList());
    }
}