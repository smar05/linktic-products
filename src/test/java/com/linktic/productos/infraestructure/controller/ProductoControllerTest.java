package com.linktic.productos.infraestructure.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linktic.productos.application.service.ProductoService;
import com.linktic.productos.domain.model.Producto;

@WebMvcTest(controllers = ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    private final ObjectMapper mapper = new ObjectMapper();
    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = Producto.builder()
                .id(3L)
                .nombre("Chaqueta hombre")
                .precio(50.23)
                .descripcion("Talla M")
                .build();
    }

    @Test
    void testCrearProducto() throws Exception {
        when(productoService.crearProducto(any(Producto.class)))
                .thenReturn(Optional.of(producto));

        mockMvc.perform(post("/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(producto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(3))
                .andExpect(jsonPath("$.data.nombre").value("Chaqueta hombre"));
    }

    @Test
    void testObtenerProductoPorId() throws Exception {
        when(productoService.obtenerProducto(3L))
                .thenReturn(Optional.of(producto));

        mockMvc.perform(get("/productos/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(3))
                .andExpect(jsonPath("$.data.nombre").value("Chaqueta hombre"));
    }

    @Test
    void testListarProductos() throws Exception {
        when(productoService.listarProductos())
                .thenReturn(Optional.of(List.of(producto)));

        mockMvc.perform(get("/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(3))
                .andExpect(jsonPath("$.data[0].nombre").value("Chaqueta hombre"));
    }

    @Test
    void testCrearProductoError() throws Exception {
        when(productoService.crearProducto(any(Producto.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(producto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testObtenerProductoNoEncontrado() throws Exception {
        when(productoService.obtenerProducto(99L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/productos/99"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testListarProductosVacio() throws Exception {
        when(productoService.listarProductos())
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/productos"))
                .andExpect(status().isBadRequest());
    }
}
