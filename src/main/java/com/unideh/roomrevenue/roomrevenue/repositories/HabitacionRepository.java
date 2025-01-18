package com.unideh.roomrevenue.roomrevenue.repositories;

import com.unideh.roomrevenue.roomrevenue.models.EstadoHabitacion;
import com.unideh.roomrevenue.roomrevenue.models.HabitacionModel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface HabitacionRepository extends CrudRepository<HabitacionModel, Long> {
    List<HabitacionModel> findByEstado(EstadoHabitacion estado);
    Optional<HabitacionModel> findByNumHabitacion(Long numHabitacion);
}
