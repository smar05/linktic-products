package com.linktic.productos.domain.ports;

import java.util.List;
import java.util.Optional;

import com.linktic.productos.domain.model.Producto;

public interface ProductoRepository {
    Producto save(final Producto producto);

    Optional<Producto> findById(final Long id);

    List<Producto> findAll();
}
