package com.unideh.roomrevenue.roomrevenue.services;

import com.unideh.roomrevenue.roomrevenue.models.ClientModel;

import com.unideh.roomrevenue.roomrevenue.models.EstatusReserva;
import com.unideh.roomrevenue.roomrevenue.models.ReservaModel;
import com.unideh.roomrevenue.roomrevenue.repositories.ClienteRepository;
import com.unideh.roomrevenue.roomrevenue.repositories.HabitacionRepository;
import com.unideh.roomrevenue.roomrevenue.repositories.ReservaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        reservaModel.setIdCliente(cliente.getId());
        reservaModel.setNumHabitacion(numHabitacion);
        cliente.setNumReserva(reservaModel.getId());


        //Guardar la reserva
        return reservaRepository.save(reservaModel);

    }

    public Optional<ReservaModel> verificarReserva(String email, Long numHabitacion){
        Optional<ClientModel> clientModel = clienteRepository.findByEmail(email);
        if(clientModel.isEmpty())
            return Optional.empty();

        Optional<ReservaModel> reservaModel = reservaRepository.findByNumHabitacionAndIdCliente(numHabitacion,clientModel.get().getId());
        if(reservaModel.isPresent() && reservaModel.get().getEstatus() == EstatusReserva.CONFIRMADA)
            return reservaModel;

        return Optional.empty();
    }
}
