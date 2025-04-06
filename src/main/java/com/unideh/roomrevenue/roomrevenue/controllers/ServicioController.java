package com.unideh.roomrevenue.roomrevenue.controllers;

import com.unideh.roomrevenue.roomrevenue.models.ServicioModel;
import com.unideh.roomrevenue.roomrevenue.repositories.ItemRepository;
import com.unideh.roomrevenue.roomrevenue.repositories.ServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/servicio")
@CrossOrigin(origins = "http://localhost:3000")
public class ServicioController {

    @Autowired
    private ServicioRepository repository;

    @Autowired
    private ItemRepository itemRepository;

    @PostMapping("/save")
    public ResponseEntity<ServicioModel> guardarServicio(@RequestBody ServicioModel servicio) {
        try {
            servicio.setHoraEntrega(LocalTime.now());

            double total = servicio.getItems().stream()
                    .mapToDouble(itemPedido -> {
                        return itemRepository.findById(itemPedido.getItemId())
                                .map(item -> item.getPrecio() * itemPedido.getCantidad())
                                .orElse(0.0);
                    })
                    .sum();

            servicio.setTotal(total);

            ServicioModel servicioGuardado = repository.save(servicio);
            return new ResponseEntity<>(servicioGuardado, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getServicioEmail/{email}/{numHabitacion}")
    public ResponseEntity<List<ServicioModel>>obtenerServicio( @PathVariable String email,
                                                               @PathVariable Long numHabitacion){

        try {
            List<ServicioModel> servicios = repository.findByEmailAndNumHabitacion(email, numHabitacion);
            if (servicios.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(servicios);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }

    }






}
