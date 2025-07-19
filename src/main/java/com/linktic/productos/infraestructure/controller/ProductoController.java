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

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/productos")
@RequiredArgsConstructor
public class ProductoController {
    private final ProductoService productoService;

    @PostMapping
    public ResponseEntity<Response> crear(@RequestBody final Producto producto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(jsonApiWrapper(productoService.crearProducto(producto)
                .orElseThrow(() -> new IllegalArgumentException("Error al crear el producto"))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> obtener(@PathVariable final Long id) {
        return productoService.obtenerProducto(id)
                .map(p -> ResponseEntity.ok(jsonApiWrapper(p)))
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + id));
    }

    @GetMapping
    public ResponseEntity<Response> listar() {
        return ResponseEntity.ok(jsonApiWrapper(productoService.listarProductos()
                .orElseThrow(() -> new IllegalArgumentException("No se encontraron productos"))));
    }

    private Response jsonApiWrapper(final Object data) {
        return Response.builder().data(data).build();
    }
}
