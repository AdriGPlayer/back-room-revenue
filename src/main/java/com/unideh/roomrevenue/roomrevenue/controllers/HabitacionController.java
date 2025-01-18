package com.unideh.roomrevenue.roomrevenue.controllers;

import com.unideh.roomrevenue.roomrevenue.models.EstadoHabitacion;
import com.unideh.roomrevenue.roomrevenue.models.HabitacionModel;
import com.unideh.roomrevenue.roomrevenue.repositories.HabitacionRepository;
import com.unideh.roomrevenue.roomrevenue.services.HabitacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/habitacion")
public class HabitacionController {

    @Autowired
    private HabitacionRepository repository;
    @Autowired
    private HabitacionService habitacionService;

    @GetMapping("getHabitacion")
    public Iterable<HabitacionModel> getAllRooms() {
        return repository.findAll();
    }


    @GetMapping("/getHabitacion/{numHabitacion}")
    public ResponseEntity<HabitacionModel> getRoomByNumber(@PathVariable long numHabitacion) {
        Optional<HabitacionModel> habitacion = repository.findByNumHabitacion(numHabitacion);
        return habitacion.map(ResponseEntity::ok)
                .orElse(ResponseEntity
                        .notFound()
                        .build());
    }
    @GetMapping("/getHabtacionesLimpias")
    public Iterable<HabitacionModel> getRoomsClean() {
        return repository.findByEstado(EstadoHabitacion.VACIA_LIMPIA);
    }

    @PutMapping("/putHabitacion/{id}")
    public ResponseEntity<HabitacionModel> updateRoom(@PathVariable long id,
                                                      @RequestBody HabitacionModel habitacionRequest) {
        Optional<HabitacionModel> optionalHabitacion = repository.findById(id);
        if (optionalHabitacion.isPresent()) {
            HabitacionModel habitacion = optionalHabitacion.get();
            habitacion.setEstado(habitacionRequest.getEstado());
            return ResponseEntity.ok(repository.save(habitacion));
        }else
            return ResponseEntity.notFound().build();
    }


    @DeleteMapping("/deleteHabitacion/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable long id){
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
