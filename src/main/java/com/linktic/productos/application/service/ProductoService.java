package com.linktic.productos.application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.linktic.productos.domain.model.Producto;
import com.linktic.productos.domain.ports.ProductoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductoService {
    private final ProductoRepository repository;

    public Optional<Producto> crearProducto(final Producto producto) {
        return Optional.ofNullable(repository.save(producto));
    }

    public Optional<Producto> obtenerProducto(final Long id) {
        return repository.findById(id);
    }

    public Optional<List<Producto>> listarProductos() {
        return Optional.ofNullable(repository.findAll());
    }
}
