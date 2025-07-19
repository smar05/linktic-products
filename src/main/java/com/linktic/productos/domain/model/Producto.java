package com.linktic.productos.domain.model;

import lombok.Data;

@Data
public class Producto {
    private Long id;
    private String nombre;
    private Double precio;
    private String descripcion;
}