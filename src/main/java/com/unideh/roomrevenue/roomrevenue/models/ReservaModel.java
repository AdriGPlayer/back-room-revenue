package com.unideh.roomrevenue.roomrevenue.models;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
@Entity
@Table(name = "reservas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private Long numHabitacion;

    private Long id_cliente;

    @Column(nullable = false)
    private LocalDate fechaEntrada;

    @Column(nullable = false)
    private LocalDate fechaSalida;


    @Column(nullable = false)
    private int numeroHuespedes;

    @Column(nullable = false)
    private double tarifa;

    @Enumerated(EnumType.STRING)
    private EstatusReserva estatus;
}
