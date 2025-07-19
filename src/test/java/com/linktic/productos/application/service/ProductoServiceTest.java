package com.linktic.productos.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.linktic.productos.domain.model.Producto;
import com.linktic.productos.domain.ports.ProductoRepository;

class ProductoServiceTest {

    @Mock
    private ProductoRepository repository;

    @InjectMocks
    private ProductoService service;

    private Producto producto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        producto = Producto.builder()
                .id(1L)
                .nombre("Zapatos")
                .precio(99.99)
                .descripcion("Talla 42")
                .build();
    }

    @Test
    void crearProducto_deberiaRetornarProductoGuardado() {
        when(repository.save(producto)).thenReturn(producto);

        Optional<Producto> result = service.crearProducto(producto);

        assertTrue(result.isPresent());
        assertEquals(producto, result.get());
        verify(repository).save(producto);
    }

    @Test
    void obtenerProducto_deberiaRetornarProductoPorId() {
        when(repository.findById(1L)).thenReturn(Optional.of(producto));

        Optional<Producto> result = service.obtenerProducto(1L);

        assertTrue(result.isPresent());
        assertEquals(producto, result.get());
        verify(repository).findById(1L);
    }

    @Test
    void listarProductos_deberiaRetornarListaDeProductos() {
        List<Producto> productos = List.of(producto);
        when(repository.findAll()).thenReturn(productos);

        Optional<List<Producto>> result = service.listarProductos();

        assertTrue(result.isPresent());
        assertEquals(1, result.get().size());
        assertEquals(producto, result.get().get(0));
        verify(repository).findAll();
    }

    @Test
    void listarProductos_deberiaRetornarOptionalVacioSiListaEsNull() {
        when(repository.findAll()).thenReturn(null);

        Optional<List<Producto>> result = service.listarProductos();

        assertTrue(result.isEmpty());
    }
}
