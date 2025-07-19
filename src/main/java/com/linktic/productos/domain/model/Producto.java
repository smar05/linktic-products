package com.linktic.productos.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Producto {
    private Long id;
    private String nombre;
    private Double precio;
    private String descripcion;
}