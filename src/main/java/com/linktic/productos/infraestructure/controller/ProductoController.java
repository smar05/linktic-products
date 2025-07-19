package com.linktic.productos.infraestructure.controller;

import java.util.Map;

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

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/productos")
@RequiredArgsConstructor
public class ProductoController {
    private final ProductoService productoService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> crear(@RequestBody final Producto producto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(jsonApiWrapper(productoService.crearProducto(producto)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtener(@PathVariable final Long id) {
        return productoService.obtenerProducto(id)
                .map(p -> ResponseEntity.ok(jsonApiWrapper(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> listar() {
        return ResponseEntity.ok(jsonApiWrapper(productoService.listarProductos()));
    }

    private Map<String, Object> jsonApiWrapper(final Object data) {
        return Map.of("data", data);
    }
}
