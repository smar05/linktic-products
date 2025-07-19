package com.linktic.productos.infraestructure.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import com.linktic.productos.domain.model.Producto;
import com.linktic.productos.domain.ports.ProductoRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductoRepositoryImpl implements ProductoRepository {
    private final ProductoJpaRepository jpa;

    @Override
    public Producto save(final Producto producto) {
        final ProductoEntity entity = toEntity(producto);
        return toModel(jpa.save(entity));
    }

    @Override
    public Optional<Producto> findById(final Long id) {
        return jpa.findById(id).map(this::toModel);
    }

    @Override
    public List<Producto> findAll() {
        return jpa.findAll().stream().map(this::toModel).toList();
    }

    private Producto toModel(final ProductoEntity entity) {
        final Producto model = new Producto();
        BeanUtils.copyProperties(entity, model);
        return model;
    }

    private ProductoEntity toEntity(final Producto model) {
        final ProductoEntity entity = new ProductoEntity();
        BeanUtils.copyProperties(model, entity);
        return entity;
    }
}
