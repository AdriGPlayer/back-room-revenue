package com.unideh.roomrevenue.roomrevenue.repositories;

import com.unideh.roomrevenue.roomrevenue.models.EstatusReserva;
import com.unideh.roomrevenue.roomrevenue.models.ReservaModel;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservaRepository extends CrudRepository<ReservaModel, Long> {
    List<ReservaModel> findByEstatus(EstatusReserva estatus);
    List<ReservaModel> findByEstatusAndFechaSalidaBetween(EstatusReserva estatus, LocalDate inicio, LocalDate fin);
    Optional<ReservaModel> findByNumHabitacionAndIdCliente(Long numHabitacion, Long idCliente);

}
