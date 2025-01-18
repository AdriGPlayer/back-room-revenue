package com.unideh.roomrevenue.roomrevenue.services;

import com.unideh.roomrevenue.roomrevenue.models.ClientModel;

import com.unideh.roomrevenue.roomrevenue.models.ReservaModel;
import com.unideh.roomrevenue.roomrevenue.repositories.ClienteRepository;
import com.unideh.roomrevenue.roomrevenue.repositories.HabitacionRepository;
import com.unideh.roomrevenue.roomrevenue.repositories.ReservaRepository;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {
    private final ReservaRepository reservaRepository;
    private final ClienteRepository clienteRepository;
    private final HabitacionRepository habitacionRepository;
    public ReservationService(ReservaRepository reservaRepository, ClienteRepository clienteRepository, HabitacionRepository habitacionRepository) {
        this.reservaRepository = reservaRepository;
        this.clienteRepository = clienteRepository;
        this.habitacionRepository = habitacionRepository;
    }

    public ReservaModel crearReserva(Long clienteId, Long numHabitacion,  ReservaModel reservaModel){
        //buscar cliente por Id
        ClientModel cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        //Asignar el cliente a la reserva
        reservaModel.setId_cliente(cliente.getId());
        reservaModel.setNumHabitacion(numHabitacion);
        cliente.setNumReserva(reservaModel.getId());


        //Guardar la reserva
        return reservaRepository.save(reservaModel);


    }
}
