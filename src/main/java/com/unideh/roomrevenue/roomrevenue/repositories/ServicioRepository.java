package com.unideh.roomrevenue.roomrevenue.repositories;

import com.unideh.roomrevenue.roomrevenue.models.ServicioModel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ServicioRepository extends CrudRepository<ServicioModel, Long> {
    List<ServicioModel> findByEmailAndNumHabitacion(String email, Long numHabitacion);
}
