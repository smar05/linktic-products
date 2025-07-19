package com.linktic.productos.infraestructure.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.linktic.productos.application.service.ProductoService;
import com.linktic.productos.domain.model.Producto;
import com.linktic.productos.domain.model.Response;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/productos")
@RequiredArgsConstructor
@Tag(name = "Productos", description = "Operaciones relacionadas con productos")
public class ProductoController {

    private final ProductoService productoService;

    @Operation(summary = "Crear un nuevo producto", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Objeto del producto a crear", required = true, content = @Content(schema = @Schema(implementation = Producto.class), examples = @ExampleObject(value = """
                {
                    "nombre": "Chaqueta hombre",
                    "precio": 50.23,
                    "descripcion": "Talla M"
                }
            """))), responses = {
            @ApiResponse(responseCode = "201", description = "Producto creado correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class), examples = @ExampleObject(value = """
                        {
                            "data": {
                                "id": 3,
                                "nombre": "Chaqueta hombre",
                                "precio": 50.23,
                                "descripcion": "Talla M"
                            }
                        }
                    """))),
            @ApiResponse(responseCode = "400", description = "Error al crear el producto")
    })
    @PostMapping
    public ResponseEntity<Response> crear(@RequestBody final Producto producto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                jsonApiWrapper(
                        productoService.crearProducto(producto)
                                .orElseThrow(() -> new IllegalArgumentException("Error al crear el producto"))));
    }

    @Operation(summary = "Obtener un producto por ID", parameters = {
            @Parameter(name = "id", description = "ID del producto", example = "3")
    }, responses = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado", content = @Content(schema = @Schema(implementation = Response.class), examples = @ExampleObject(value = """
                        {
                            "data": {
                                "id": 3,
                                "nombre": "Chaqueta hombre",
                                "precio": 50.23,
                                "descripcion": "Talla M"
                            }
                        }
                    """))),
            @ApiResponse(responseCode = "400", description = "Producto no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Response> obtener(
            @Parameter(description = "ID del producto", required = true) @PathVariable final Long id) {

        return productoService.obtenerProducto(id)
                .map(p -> ResponseEntity.ok(jsonApiWrapper(p)))
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + id));
    }

    @Operation(summary = "Listar todos los productos", responses = {
            @ApiResponse(responseCode = "200", description = "Lista de productos", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Producto.class)), examples = @ExampleObject(value = """
                        {
                            "data": [
                                {
                                    "id": 1,
                                    "nombre": "Zapatos deportivos",
                                    "precio": 75.5,
                                    "descripcion": "Color negro, talla 42"
                                },
                                {
                                    "id": 3,
                                    "nombre": "Chaqueta hombre",
                                    "precio": 50.23,
                                    "descripcion": "Talla M"
                                }
                            ]
                        }
                    """))),
            @ApiResponse(responseCode = "400", description = "No se encontraron productos")
    })
    @GetMapping
    public ResponseEntity<Response> listar() {
        return ResponseEntity.ok(
                jsonApiWrapper(
                        productoService.listarProductos()
                                .orElseThrow(() -> new IllegalArgumentException("No se encontraron productos"))));
    }

    private Response jsonApiWrapper(final Object data) {
        return Response.builder().data(data).build();
    }
}
