package com.unideh.roomrevenue.roomrevenue.controllers;

import com.unideh.roomrevenue.roomrevenue.models.ClientModel;
import com.unideh.roomrevenue.roomrevenue.models.EstatusReserva;
import com.unideh.roomrevenue.roomrevenue.models.HabitacionModel;
import com.unideh.roomrevenue.roomrevenue.models.ReservaModel;
import com.unideh.roomrevenue.roomrevenue.repositories.ClienteRepository;
import com.unideh.roomrevenue.roomrevenue.repositories.HabitacionRepository;
import com.unideh.roomrevenue.roomrevenue.repositories.ReservaRepository;
import com.unideh.roomrevenue.roomrevenue.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private ReservaRepository repository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private HabitacionRepository habitacionRepository;

    @GetMapping("/getReserva")
    public Iterable<ReservaModel> getReservation(){
        return repository.findAll();
    }


    @GetMapping("/getReserva/{id}")
    public ResponseEntity<ReservaModel> getReservationById(@PathVariable long id){
        Optional<ReservaModel> reserva = repository.findById(id);
        return reserva.map(ResponseEntity::ok)
                .orElse(ResponseEntity
                        .notFound()
                        .build());
    }
    @PostMapping("/postReserva/{idCliente}/{numHabitacion}")
    public ReservaModel  saveReserva(@PathVariable Long idCliente,
                            @PathVariable Long numHabitacion,
                            @RequestBody ReservaModel reservaModel) {

        // Buscar cliente por ID
        ClientModel cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + idCliente));
        // Asignar el cliente a la reserva
        reservaModel.setId_cliente(cliente.getId());
        reservaModel.setNumHabitacion(numHabitacion);

        // Buscar habitación por número
        Optional<HabitacionModel> habitacion = habitacionRepository.findByNumHabitacion(numHabitacion);
        if (habitacion == null) {
            throw new RuntimeException("Habitación no encontrada con número: " + numHabitacion);
        }

        // Guardar la reserva
        return repository.save(reservaModel);
    }

    @PutMapping("/confirmarReserva/{id}")
    public ResponseEntity<ReservaModel> confirmarReserva(@PathVariable Long id) {
        Optional<ReservaModel> reserva = repository.findById(id);
        if(!reserva.isPresent())
            return ResponseEntity.notFound().build();
        ReservaModel reservaRequest = reserva.get();
        if(reservaRequest.getEstatus() == EstatusReserva.PENDIENTE){
            reservaRequest.setEstatus(EstatusReserva.CONFIRMADA);
            repository.save(reservaRequest);
            return ResponseEntity.ok(reservaRequest);
        }else
            return ResponseEntity.badRequest().build();
    }




}