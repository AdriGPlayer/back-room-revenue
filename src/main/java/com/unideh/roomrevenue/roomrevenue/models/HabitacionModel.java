package com.unideh.roomrevenue.roomrevenue.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HabitacionModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long numHabitacion;

    @Enumerated(EnumType.STRING)
    private TipoHabitacion tipoHabitacion;

    @Enumerated(EnumType.STRING)
    private EstadoHabitacion estado;
}
