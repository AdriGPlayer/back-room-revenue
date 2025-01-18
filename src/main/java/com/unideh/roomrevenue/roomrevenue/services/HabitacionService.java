package com.unideh.roomrevenue.roomrevenue.services;

import com.unideh.roomrevenue.roomrevenue.models.HabitacionModel;
import com.unideh.roomrevenue.roomrevenue.repositories.HabitacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HabitacionService {
    @Autowired
    private HabitacionRepository habitacionRepository;

    public Optional<HabitacionModel> getHabitacionByNumHabitacion(Long numHabitacion) {
        return habitacionRepository.findByNumHabitacion(numHabitacion).stream().findFirst();
    }
}