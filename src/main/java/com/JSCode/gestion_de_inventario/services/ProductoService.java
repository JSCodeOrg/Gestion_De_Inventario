package com.JSCode.gestion_de_inventario.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.JSCode.gestion_de_inventario.dto.ImagesDTO;
import com.JSCode.gestion_de_inventario.dto.Response.ApiResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import com.JSCode.gestion_de_inventario.dto.productos.AgregarCantidadDTO;
import com.JSCode.gestion_de_inventario.dto.productos.AgregarProductNuevoDTO;
import com.JSCode.gestion_de_inventario.dto.productos.CategoriaDTO;
import com.JSCode.gestion_de_inventario.dto.productos.EditarProductoDTO;
import com.JSCode.gestion_de_inventario.dto.productos.ProductoCarruselDTO;
import com.JSCode.gestion_de_inventario.dto.productos.ProductoDTO;
import com.JSCode.gestion_de_inventario.dto.productos.ProductoResumenDTO;
import com.JSCode.gestion_de_inventario.exceptions.ResourceNotFoundException;
import com.JSCode.gestion_de_inventario.models.Categoria;
import com.JSCode.gestion_de_inventario.models.Imagenes;
import com.JSCode.gestion_de_inventario.models.Productos;
import com.JSCode.gestion_de_inventario.repositories.CategoriaRepository;
import com.JSCode.gestion_de_inventario.repositories.ImagenesRepository;
import com.JSCode.gestion_de_inventario.repositories.ProductoRepository;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ImagenesRepository imagenesRepository;

    @Autowired
    private ImageStorageService imgService;

    public List<ProductoResumenDTO> filtrarProductos(String categoria, BigDecimal precioMin,
            BigDecimal precioMax) {
        Specification<Productos> spec = Specification.where(null);

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

        return productos.stream().map(producto -> {
            List<ImagesDTO> imagenesDTO = producto.getImagenes().stream()
                    .map(imagen -> {
                        ImagesDTO dto = new ImagesDTO();
                        dto.setId(imagen.getId());
                        dto.setUrl(imagen.getImageUrl());
                        return dto;
                    })
                    .collect(Collectors.toList());

            return new ProductoResumenDTO(
                    producto.getId(),
                    producto.getNombre(),
                    producto.getDescripcion(),
                    producto.getCategoria(),
                    producto.getPrecioCompra(),
                    imagenesDTO);
        }).toList();
    }

    public Page<ProductoResumenDTO> buscarPorTexto(String texto, Pageable pageable) {
        String keyword = "%" + texto.toLowerCase() + "%";

        Page<Productos> productosPage = productoRepository.findAll((root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("nombre")), keyword),
                cb.like(cb.lower(root.get("descripcion")), keyword),
                cb.like(cb.lower(root.get("categoria").get("nombreCategoria")), keyword),
                cb.like(cb.lower(root.get("palabrasClave")), keyword)), pageable);

        return productosPage.map(producto -> {
            List<ImagesDTO> imagenesDTO = producto.getImagenes().stream()
                    .map(imagen -> {
                        ImagesDTO dto = new ImagesDTO();
                        dto.setId(imagen.getId());
                        dto.setUrl(imagen.getImageUrl());
                        return dto;
                    })
                    .collect(Collectors.toList());

            return new ProductoResumenDTO(
                    producto.getId(),
                    producto.getNombre(),
                    producto.getDescripcion(),
                    producto.getCategoria(),
                    producto.getPrecioCompra(),
                    imagenesDTO);
        });
    }

    public ApiResponse<String> productoEliminar(Long id) {
        productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));
        productoRepository.deleteById(id);
        return new ApiResponse<String>("Producto Eliminado con Exito", false, 200);
    }

    public ProductoDTO actualizarProducto(EditarProductoDTO productoDTO, Long id,
            List<MultipartFile> imagenesAñadidas) {

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

        List<ImagesDTO> nuevasImagenesDTO = new ArrayList<>();
        if (imagenesAñadidas != null && !imagenesAñadidas.isEmpty()) {
            List<String> urls = imgService.uploadImagesToImgBB(imagenesAñadidas);
            for (String url : urls) {
                Imagenes newImage = new Imagenes();
                newImage.setProducto(producto);
                newImage.setImageUrl(url);
                Imagenes savedImage = this.imagenesRepository.save(newImage);
                nuevasImagenesDTO.add(toDTO(savedImage));
            }
        }

        List<ImagesDTO> eliminadasDTO = new ArrayList<>();
        if (productoDTO.getImagenesEliminadas() != null && !productoDTO.getImagenesEliminadas().isEmpty()) {
            for (Long image_id : productoDTO.getImagenesEliminadas()) {
                Imagenes image = this.imagenesRepository.findById(image_id)
                        .orElseThrow(
                                () -> new ResourceNotFoundException("Imagen no encontrada con el id: " + image_id));
                eliminadasDTO.add(toDTO(image));
                this.imagenesRepository.delete(image);
            }
        }

        productoRepository.save(producto);

        // Obtener imágenes actuales
        List<Imagenes> imagenesActuales = imagenesRepository.findByProductoId(producto.getId());
        List<ImagesDTO> urlsActualesDTO = imagenesActuales.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        // Construir el DTO de respuesta
        ProductoDTO editedProduct = new ProductoDTO();
        editedProduct.setNombre(producto.getNombre());
        editedProduct.setDescripcion(producto.getDescripcion());
        editedProduct.setCantidadDisponible(producto.getCantidadDisponible());
        editedProduct.setPrecioCompra(producto.getPrecioCompra());
        editedProduct.setStockMinimo(producto.getStockMinimo());
        editedProduct.setPalabrasClave(producto.getPalabrasClave());

        editedProduct.setUrlsImagenes(urlsActualesDTO);
        editedProduct.setNewImages(nuevasImagenesDTO);
        editedProduct.setDeletedImages(eliminadasDTO);

        return editedProduct;
    }

    public ImagesDTO toDTO(Imagenes imagen) {
        ImagesDTO dto = new ImagesDTO();
        dto.setId(imagen.getId());
        dto.setUrl(imagen.getImageUrl());
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

        List<ImagesDTO> imagenesDTO = producto.getImagenes().stream()
                .map(imagen -> {
                    ImagesDTO imagenDTO = new ImagesDTO();
                    imagenDTO.setId(imagen.getId());
                    imagenDTO.setUrl(imagen.getImageUrl());
                    return imagenDTO;
                })
                .collect(Collectors.toList());

        dto.setUrlsImagenes(imagenesDTO);

        return dto;
    }

    public List<ProductoCarruselDTO> obtenerProductosCarrusel(Long categoria_id) {

        Categoria categoria = this.categoriaRepository.findById(categoria_id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + categoria_id));

        List<Productos> productos = this.productoRepository.findTop15ByCategoriaOrderByIdAsc(categoria);

        return productos.stream()
                .map(producto -> {
                    ProductoCarruselDTO dto = new ProductoCarruselDTO();
                    dto.setId(producto.getId());

                    if (!producto.getImagenes().isEmpty()) {
                        List<ImagesDTO> imagenesDTO = producto.getImagenes().stream()
                                .map(imagen -> {
                                    ImagesDTO imagenDTO = new ImagesDTO();
                                    imagenDTO.setId(imagen.getId());
                                    imagenDTO.setUrl(imagen.getImageUrl());
                                    return imagenDTO;
                                })
                                .collect(Collectors.toList());

                        dto.setImagenes(imagenesDTO);
                    } else {
                        dto.setImagenes(Collections.emptyList());
                    }

                    dto.setCategoria(producto.getCategoria());
                    dto.setDescripcion(producto.getDescripcion());
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

    @Transactional
    public ProductoDTO agregarProductoNuevo(AgregarProductNuevoDTO productoDTO) {
        Categoria categoria = categoriaRepository.findById(productoDTO.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Categoría no encontrada con ID: " + productoDTO.getCategoriaId()));

        Productos nuevoProducto = new Productos();
        nuevoProducto.setNombre(productoDTO.getNombre());
        nuevoProducto.setDescripcion(productoDTO.getDescripcion());
        nuevoProducto.setCantidadDisponible(productoDTO.getCantidadDisponible());
        nuevoProducto.setPrecioCompra(productoDTO.getPrecioCompra());
        nuevoProducto.setStockMinimo(productoDTO.getStockMinimo());
        nuevoProducto.setPalabrasClave(productoDTO.getPalabrasClave());
        nuevoProducto.setCategoria(categoria);

        Productos productoGuardado = productoRepository.save(nuevoProducto);
        if (productoDTO.getImagenesUrls() != null && !productoDTO.getImagenesUrls().isEmpty()) {
            for (ImagesDTO imagenDTO : productoDTO.getImagenesUrls()) {
                Imagenes imagen = new Imagenes();
                imagen.setImageUrl(imagenDTO.getUrl());
                imagen.setProducto(productoGuardado);
                imagenesRepository.save(imagen);
            }
        }

        ProductoDTO responseDTO = new ProductoDTO();
        responseDTO.setNombre(productoGuardado.getNombre());
        responseDTO.setDescripcion(productoGuardado.getDescripcion());
        responseDTO.setCantidadDisponible(productoGuardado.getCantidadDisponible());
        responseDTO.setPrecioCompra(productoGuardado.getPrecioCompra());
        responseDTO.setStockMinimo(productoGuardado.getStockMinimo());
        responseDTO.setPalabrasClave(productoGuardado.getPalabrasClave());
        responseDTO.setCategoriaId(productoGuardado.getCategoria().getId());

        List<ImagesDTO> imagenesDTO = productoGuardado.getImagenes().stream()
                .map(imagen -> {
                    ImagesDTO imgDTO = new ImagesDTO();
                    imgDTO.setId(imagen.getId());
                    imgDTO.setUrl(imagen.getImageUrl());
                    return imgDTO;
                })
                .collect(Collectors.toList());
        responseDTO.setUrlsImagenes(imagenesDTO);

        return responseDTO;
    }

}