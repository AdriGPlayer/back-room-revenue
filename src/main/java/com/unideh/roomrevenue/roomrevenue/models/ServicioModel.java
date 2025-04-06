package com.unideh.roomrevenue.roomrevenue.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "servicios")
@Data
public class ServicioModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoServicio tipoServicio;

    @ElementCollection
    @CollectionTable(
            name = "servicio_items",
            joinColumns = @JoinColumn(name = "servicio_id")
    )
    private List<ItemPedido> items;

    private String detalles;
    private Double total;
    private LocalTime horaEntrega;
    private String email;
    private Long numHabitacion;
}
