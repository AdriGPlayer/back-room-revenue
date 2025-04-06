package com.unideh.roomrevenue.roomrevenue.models;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "pedidos")
@Data
public class Pedidos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idServicio;
    private Long idCliente;



}
