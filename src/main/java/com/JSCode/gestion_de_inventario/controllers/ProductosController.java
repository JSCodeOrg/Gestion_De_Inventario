package com.JSCode.gestion_de_inventario.controllers;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import com.JSCode.gestion_de_inventario.dto.Response.Response;
import com.JSCode.gestion_de_inventario.dto.productos.AgregarCantidadDTO;
import com.JSCode.gestion_de_inventario.dto.productos.AgregarProductNuevoDTO;
import com.JSCode.gestion_de_inventario.dto.productos.CategoriaDTO;
import com.JSCode.gestion_de_inventario.dto.productos.EditarProductoDTO;
import com.JSCode.gestion_de_inventario.dto.productos.ProductoCarruselDTO;
import com.JSCode.gestion_de_inventario.dto.productos.ProductoDTO;
import com.JSCode.gestion_de_inventario.dto.productos.ProductoResumenDTO;
import com.JSCode.gestion_de_inventario.services.ProductoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;

@RestController
@RequestMapping("/productos")
@Tag(name = "Productos", description = "Operaciones relacionadas con los productos y categorias")
public class ProductosController {

    @Autowired
    private ProductoService productoService;

    // Filtrar productos

    @Operation(
    summary = "Filtrar productos",
    description = "Devuelve una lista de productos que cumplen con los filtros opcionales: categoría, precio mínimo y máximo"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Productos filtrados con éxito",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Response.class),
                examples = @ExampleObject(
                    name = "Lista de productos",
                    value = """
                        {
                        "message": "Productos filtrados con éxito",
                        "data": [
                            {
                            "id": 1,
                            "nombre": "Audífonos Bluetooth",
                            "precio": 150000.00,
                            "categoria": "Tecnología"
                            },
                            {
                            "id": 2,
                            "nombre": "Mouse inalámbrico",
                            "precio": 35000.00,
                            "categoria": "Tecnología"
                            }
                        ],
                        "error": false,
                        "status": 200
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno al filtrar productos",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Error interno",
                    value = """
                        {
                        "message": "No se pudo procesar la solicitud",
                        "data": null,
                        "error": true,
                        "status": 500
                        }
                        """
                )
            )
        )
    })
    @GetMapping("/filtrar")
    public ResponseEntity<Response<List<ProductoResumenDTO>>> filtrarProductos(
            @Parameter(description = "Categoría del producto (opcional)", example = "Tecnología")
            @RequestParam(required = false) String categoria,
            @Parameter(description = "Precio mínimo (opcional)", example = "10000.00")
            @RequestParam(required = false) BigDecimal precioMin,
            @Parameter(description = "Precio máximo (opcional)", example = "500000.00")
            @RequestParam(required = false) BigDecimal precioMax) {

        List<ProductoResumenDTO> productos = productoService.filtrarProductos(categoria, precioMin, precioMax);
        return ResponseEntity.ok(new Response<>("Productos filtrados con éxito", productos, false, 200));
    }

    // Buscar productos

    @Operation(
    summary = "Buscar productos",
    description = "Busca productos que coincidan con un texto dado en su nombre o descripción. Los resultados se entregan paginados."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Productos encontrados con éxito",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Page.class),
                examples = @ExampleObject(
                    name = "Respuesta exitosa",
                    value = """
                        {
                        "content": [
                            {
                            "id": 5,
                            "nombre": "Teclado Mecánico RGB",
                            "precio": 180000.0,
                            "categoria": "Accesorios"
                            },
                            {
                            "id": 6,
                            "nombre": "Mousepad XL",
                            "precio": 35000.0,
                            "categoria": "Accesorios"
                            }
                        ],
                        "pageable": {
                            "pageNumber": 0,
                            "pageSize": 2
                        },
                        "totalPages": 1,
                        "totalElements": 2,
                        "last": true,
                        "size": 2,
                        "number": 0,
                        "sort": {},
                        "first": true,
                        "numberOfElements": 2,
                        "empty": false
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno al realizar la búsqueda",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Error interno",
                    value = """
                        {
                        "message": "Error al buscar productos",
                        "data": null,
                        "error": true,
                        "status": 500
                        }
                        """
                )
            )
        )
    })
    @GetMapping("/buscar")
    public ResponseEntity<Page<ProductoResumenDTO>> buscarProductos(
        @Parameter(description = "Texto a buscar en el nombre o descripción del producto", example = "teclado")
        @RequestParam String texto,
        @Parameter(description = "Número de página (empezando desde 0)", example = "0")
        @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Tamaño de página", example = "10")
        @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductoResumenDTO> productos = productoService.buscarPorTexto(texto, pageable);
        return ResponseEntity.ok(productos);
    }

    // Actualizar productos

    @Operation(
    summary = "Actualizar producto",
    description = "Actualiza los datos de un producto existente. Se permite actualizar las imágenes si se adjuntan nuevas."
)
@ApiResponses(value = {
    @ApiResponse(
        responseCode = "200",
        description = "Producto actualizado con éxito",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = Response.class),
            examples = @ExampleObject(
                value = """
                {
                  "message": "Producto actualizado con éxito",
                  "data": {
                    "id": 5,
                    "nombre": "Teclado mecánico",
                    "descripcion": "Teclado mecánico RGB retroiluminado",
                    "categoria": {
                      "id": 2,
                      "nombre": "Accesorios"
                    },
                    "precioCompra": 150000.00,
                    "imagenes": [
                      {
                        "id": 21,
                        "url": "https://miapp.com/images/teclado.png"
                      }
                    ]
                  },
                  "error": false,
                  "status": 200
                }
                """
            )
        )
    ),
    @ApiResponse(
        responseCode = "400",
        description = "Solicitud inválida",
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(
                value = """
                {
                  "message": "Faltan datos del producto",
                  "data": null,
                  "error": true,
                  "status": 400
                }
                """
            )
        )
    ),
    @ApiResponse(
        responseCode = "403",
        description = "Acceso denegado - solo administradores",
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(
                value = """
                {
                  "message": "Acceso denegado",
                  "data": null,
                  "error": true,
                  "status": 403
                }
                """
            )
        )
    ),
    @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor",
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(
                value = """
                {
                  "message": "Error del servidor",
                  "data": null,
                  "error": true,
                  "status": 500
                }
                """
            )
        )
    )
})
    @Parameters({
    @Parameter(name = "id", description = "ID del producto a actualizar", required = true, example = "5"),
    @Parameter(
        name = "producto",
        description = "Datos actualizados del producto",
        required = true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = EditarProductoDTO.class)
        )
    ),
    @Parameter(
        name = "imagenes",
        description = "Lista de nuevas imágenes para el producto (opcional)",
        required = false,
        content = @Content(mediaType = "image/*")
    )
})

    @PreAuthorize("hasRole('administrador')")
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Response<ProductoDTO>> actualizarProducto(@PathVariable Long id,
        @RequestPart("producto") EditarProductoDTO productoInfo, @RequestPart(value = "imagenes", required = false) List<MultipartFile> imagenesAñadidas) {

        ProductoDTO editedProduct = productoService.actualizarProducto(productoInfo, id, imagenesAñadidas);

        return ResponseEntity.ok(new Response<>("Producto actualizado con éxito", editedProduct, false, 200));

    }

    //eliminar productos

    @Operation(
    summary = "Eliminar un producto",
    description = "Permite eliminar un producto del sistema dado su ID. Solo puede ser ejecutado por usuarios con el rol de administrador."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Producto eliminado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(
                implementation = Response.class
            ), examples = @ExampleObject(value = """
                {
                "message": null,
                "data": {
                    "message": "Producto eliminado correctamente",
                    "data": "ID 12 eliminado",
                    "error": false,
                    "status": 200
                },
                "error": false,
                "status": 200
                }
                """))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Producto no encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class),
            examples = @ExampleObject(value = """
                {
                "message": "Producto no encontrado",
                "data": null,
                "error": true,
                "status": 404
                }
            """))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class),
            examples = @ExampleObject(value = """
                {
                "message": "Error al eliminar el producto",
                "data": null,
                "error": true,
                "status": 500
                }
            """))
        )
    })
    @Parameter(
        name = "id",
        description = "ID del producto a eliminar",
        required = true,
        example = "12"
    )

    @PreAuthorize("hasRole('administrador')")
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Response<Response<String>>> eliminarProducto(@PathVariable Long id) {
        Response<String> productoEliminar = productoService.productoEliminar(id);

        return ResponseEntity.ok(new Response<>(null, productoEliminar, false, 200));

    }

    // Agregar unidades a un producto

    @Operation(
    summary = "Agregar unidades a un producto",
    description = "Agrega una cantidad específica de unidades a un producto dado su ID."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Unidades agregadas correctamente al producto",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class),
            examples = @ExampleObject(value = """
                {
                "message": "Producto agregado con éxito",
                "data": null,
                "error": false,
                "status": 200
                }
            """))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "ID inválido o cantidad no válida",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class),
            examples = @ExampleObject(value = """
                {
                "message": "Cantidad inválida o producto no encontrado",
                "data": null,
                "error": true,
                "status": 400
                }
            """))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno al agregar unidades",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class),
            examples = @ExampleObject(value = """
                {
                "message": "Error inesperado al agregar unidades al producto",
                "data": null,
                "error": true,
                "status": 500
                }
            """))
        )
    })
    @Parameter(
        name = "id",
        description = "ID del producto al que se desea agregar unidades",
        required = true,
        example = "10"
    )
    @PostMapping("/agregar/{id}")
    public ResponseEntity<Response<String>> agregarUnidadesProducto(
        @PathVariable Long id,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "DTO con la cantidad a agregar",
            content = @Content(
                schema = @Schema(implementation = AgregarCantidadDTO.class),
                examples = @ExampleObject(value = """
                    {
                      "cantidad": 20
                    }
                """)
            )
        )    
        @RequestBody AgregarCantidadDTO cantidadDTO) {
        productoService.agregarUnidadesProducto(id, cantidadDTO);
        return ResponseEntity.ok(new Response<>("Producto agregado con éxito", false, 200));
    }

    // Ver información de un producto

    @Operation(
    summary = "Obtener detalles de un producto",
    description = "Retorna la información detallada de un producto dado su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Producto encontrado con éxito",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class),
            examples = @ExampleObject(value = """
                {
                "message": "Producto encontrado con éxito",
                "data": {
                    "productoId": 12,
                    "nombre": "Monitor Samsung",
                    "descripcion": "Monitor LED 24 pulgadas",
                    "precioCompra": 560000,
                    "stockMinimo": 5,
                    "cantidadDisponible": 30,
                    "categoria": {
                    "id": 3,
                    "nombre": "Tecnología"
                    },
                    "imagenes": [
                    {
                        "url": "https://miapp.com/images/monitor.jpg"
                    }
                    ]
                },
                "error": false,
                "status": 200
                }
            """))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Producto no encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class),
            examples = @ExampleObject(value = """
                {
                "message": "Producto no encontrado",
                "data": null,
                "error": true,
                "status": 404
                }
            """))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno al obtener el producto",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class),
            examples = @ExampleObject(value = """
                {
                "message": "Error al consultar el producto",
                "data": null,
                "error": true,
                "status": 500
                }
            """))
        )
    })
    @Parameter(
        name = "id",
        description = "ID del producto que se desea consultar",
        required = true,
        example = "12"
    )

    @GetMapping("/ver/{id}")
    public ResponseEntity<Response<ProductoDTO>> verProducto(@PathVariable Long id) {
        ProductoDTO producto = productoService.verProducto(id);
        return ResponseEntity.ok(new Response<>("Producto encontrado con éxito", producto, false, 200));
    }

    // Obtener los productos de una categoria

    @Operation(
    summary = "Obtener productos por categoría",
    description = "Devuelve una lista de productos en formato carrusel según la categoría proporcionada"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Productos obtenidos correctamente",
            content = @Content(
                mediaType = "application/json",
                array = @io.swagger.v3.oas.annotations.media.ArraySchema(schema = @Schema(implementation = ProductoCarruselDTO.class)),
                examples = @ExampleObject(value = """
                    [
                    {
                        "id": 10,
                        "nombre": "Audífonos Bluetooth",
                        "imagenPrincipal": "https://miapp.com/images/audifonos.jpg",
                        "precioCompra": 75000
                    },
                    {
                        "id": 11,
                        "nombre": "Teclado Mecánico",
                        "imagenPrincipal": "https://miapp.com/images/teclado.jpg",
                        "precioCompra": 120000
                    }
                    ]
                """)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "No se encontraron productos para la categoría indicada",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = """
                    []
                """))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error al obtener productos por categoría",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = """
                    {
                    "message": "Error al consultar productos",
                    "data": null,
                    "error": true,
                    "status": 500
                    }
                """))
        )
    })
    @Parameter(
        name = "categoria_id",
        description = "ID de la categoría para filtrar productos",
        required = true,
        example = "3"
    )
    @GetMapping("/obtener/categoria")
    public ResponseEntity<List<ProductoCarruselDTO>> obtenerPorCategoria(@RequestParam Long categoria_id) {
        List<ProductoCarruselDTO> productos = this.productoService.obtenerProductosCarrusel(categoria_id);
        return ResponseEntity.ok(productos);
    }

    // Obtener las categorias

    @Operation(
    summary = "Obtener todas las categorías de productos",
    description = "Retorna una lista con todas las categorías disponibles en la tienda"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Categorías obtenidas exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Response.class),
                examples = @ExampleObject(value = """
                    {
                    "message": "Categorias obtenidas con éxito",
                    "data": [
                        {
                        "id": 1,
                        "nombre": "Tecnología",
                        "descripcion": "Productos relacionados con tecnología y electrónica"
                        },
                        {
                        "id": 2,
                        "nombre": "Hogar",
                        "descripcion": "Artículos para el hogar y cocina"
                        }
                    ],
                    "error": false,
                    "status": 200
                    }
                """)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno al obtener las categorías",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = """
                    {
                    "message": "Error al consultar categorías",
                    "data": null,
                    "error": true,
                    "status": 500
                    }
                """))
        )
    })
    @GetMapping("/categorias")
    public ResponseEntity<Response<List<CategoriaDTO>>> obtenerCategorias() {
        List<CategoriaDTO> categorias = productoService.obtenerCategorias();
        return ResponseEntity.ok(new Response<>("Categorias obtenidas con éxito", categorias, false, 200));
    }

    // Agregar nuevos productos

    @Operation(
    summary = "Agregar nuevo producto",
    description = "Permite registrar un nuevo producto en el sistema. Solo accesible para usuarios con rol administrador."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Producto agregado con éxito",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Response.class),
                examples = @ExampleObject(value = """
                    {
                    "message": "Producto agregado con éxito",
                    "data": {
                        "id": 12,
                        "nombre": "Monitor LG 24 pulgadas",
                        "descripcion": "Monitor Full HD con HDMI y VGA",
                        "categoria": {
                        "id": 3,
                        "nombre": "Tecnología",
                        "descripcion": "Productos electrónicos"
                        },
                        "precioCompra": 550000,
                        "imagenes": [
                        {
                            "url": "https://example.com/imagenes/monitor1.jpg"
                        }
                        ]
                    },
                    "error": false,
                    "status": 201
                    }
                """)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Solicitud inválida o datos incompletos",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(value = """
                    {
                    "message": "Campos obligatorios faltantes: nombre, precioCompra",
                    "data": null,
                    "error": true,
                    "status": 400
                    }
                """)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(value = """
                    {
                    "message": "Error al agregar el producto",
                    "data": null,
                    "error": true,
                    "status": 500
                    }
                """)
            )
        )
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
    description = "Cuerpo de la solicitud con la información del nuevo producto",
    required = true,
    content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = AgregarProductNuevoDTO.class),
        examples = @ExampleObject(value = """
            {
              "nombre": "Monitor LG 24 pulgadas",
              "descripcion": "Monitor Full HD con entrada HDMI y VGA",
              "categoriaId": 3,
              "precioCompra": 550000,
              "stockMinimo": 5,
              "cantidadDisponible": 20,
              "palabrasClave": "monitor, tecnología, lg"
            }
        """)
        )
    )
    @PreAuthorize("hasRole('administrador')")
    @PostMapping("/agregar/nuevoproducto")
    public ResponseEntity<Response<ProductoDTO>> agregarProductoNuevo(
            @RequestBody AgregarProductNuevoDTO productoDTO) {
        ProductoDTO productoAgregado = productoService.agregarProductoNuevo(productoDTO);
        return ResponseEntity.ok(new Response<>("Producto agregado con éxito", productoAgregado, false, 201));
    }
}
