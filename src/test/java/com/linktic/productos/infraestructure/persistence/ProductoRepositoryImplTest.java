package com.linktic.productos.infraestructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.linktic.productos.domain.model.Producto;
import com.linktic.productos.domain.ports.ProductoRepository;

class ProductoRepositoryImplTest {

    private ProductoJpaRepository jpaRepository;
    private ProductoRepository repository;

    private Producto producto;
    private ProductoEntity entity;

    @BeforeEach
    void setUp() {
        jpaRepository = mock(ProductoJpaRepository.class);
        repository = new ProductoRepositoryImpl(jpaRepository);

        producto = Producto.builder()
                .id(1L)
                .nombre("Camisa")
                .precio(30.5)
                .descripcion("Manga corta")
                .build();

        entity = new ProductoEntity();
        entity.setId(1L);
        entity.setNombre("Camisa");
        entity.setPrecio(30.5);
        entity.setDescripcion("Manga corta");
    }

    @Test
    void saveShouldPersistAndReturnProducto() {
        when(jpaRepository.save(any(ProductoEntity.class))).thenReturn(entity);

        Producto result = repository.save(producto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getNombre()).isEqualTo("Camisa");

        ArgumentCaptor<ProductoEntity> captor = ArgumentCaptor.forClass(ProductoEntity.class);
        verify(jpaRepository).save(captor.capture());
        assertThat(captor.getValue().getNombre()).isEqualTo("Camisa");
    }

    @Test
    void findByIdShouldReturnProducto() {
        when(jpaRepository.findById(1L)).thenReturn(Optional.of(entity));

        Optional<Producto> result = repository.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getNombre()).isEqualTo("Camisa");
    }

    @Test
    void findAllShouldReturnListOfProductos() {
        when(jpaRepository.findAll()).thenReturn(List.of(entity));

        List<Producto> result = repository.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNombre()).isEqualTo("Camisa");
    }
}
