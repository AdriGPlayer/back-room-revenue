package com.unideh.roomrevenue.roomrevenue.controllers;

import com.unideh.roomrevenue.roomrevenue.models.*;
import com.unideh.roomrevenue.roomrevenue.repositories.ClienteRepository;
import com.unideh.roomrevenue.roomrevenue.repositories.HabitacionRepository;
import com.unideh.roomrevenue.roomrevenue.repositories.ReservaRepository;
import com.unideh.roomrevenue.roomrevenue.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
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

    @Autowired
    private ReservationService service;

    private double calcularIngresos(YearMonth mes){
        LocalDate inicioMes = mes.atDay(1);
        LocalDate finMes = mes.atEndOfMonth();

        List<ReservaModel> reservas = repository.findByEstatusAndFechaSalidaBetween(EstatusReserva.COMPLETADA,
                inicioMes,
                finMes);
        return reservas.stream().mapToDouble(ReservaModel::getTarifa).sum();
    }

    private double calcularPorcentajeCambio(double ingresosActuales, double ingresosAnteriores){
        if(ingresosAnteriores == 0)
            return ingresosActuales > 0 ? 100.0 : -100.0;
        double cambio = ((ingresosActuales - ingresosAnteriores) / ingresosAnteriores) * 100;
        return BigDecimal.valueOf(cambio).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
    private record IngresosResponse(double ingresosActuales, double ingresosAnteriores, double porcentajeCambio) {}


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

    @GetMapping("/getReserva-huesped/{numHabitacion}/{email}")
    public  ResponseEntity<?> verificarReserva(@PathVariable long numHabitacion, @PathVariable String email) {
        Optional<ReservaModel> reserva = service.verificarReserva(email, numHabitacion);
        if(reserva.isPresent())
            return ResponseEntity.ok(reserva.get());
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró una reserva para este cliente en la habitación especificada.");

    }
    @GetMapping("/getReservasConfirmadas")
    public ResponseEntity<List<ReservaModel>> getReservasConfirmadas() {
        List<ReservaModel> reservasConfirmadas = repository.findByEstatus(EstatusReserva.CONFIRMADA);

        if (reservasConfirmadas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(reservasConfirmadas);
    }

    @PostMapping("/postReserva/{idCliente}/{numHabitacion}")
    public ReservaModel  saveReserva(@PathVariable Long idCliente,
                            @PathVariable Long numHabitacion,
                            @RequestBody ReservaModel reservaModel) {

        // Buscar cliente por ID
        ClientModel cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + idCliente));
        // Asignar el cliente a la reserva
        reservaModel.setIdCliente(cliente.getId());
        reservaModel.setNumHabitacion(numHabitacion);

        // Buscar habitación por número
        Optional<HabitacionModel> habitacion = habitacionRepository.findByNumHabitacion(numHabitacion);
        if (habitacion == null) {
            throw new RuntimeException("Habitación no encontrada con número: " + numHabitacion);
        }

        // Guardar la reserva
        return repository.save(reservaModel);
    }




    @GetMapping("/ingresos-mensuales")
    public ResponseEntity<?> obtenerIngresosMensuales() {

        YearMonth mesActual = YearMonth.now();
        YearMonth mesAnterior = mesActual.minusMonths(1);

        double ingresosActuales = calcularIngresos(mesActual);

        double ingresosAnteriores = calcularIngresos(mesAnterior);

        double porcentajeCambio = calcularPorcentajeCambio(ingresosActuales, ingresosAnteriores);

        return ResponseEntity.ok(new IngresosResponse(ingresosActuales, ingresosAnteriores, porcentajeCambio));


    }

    @DeleteMapping("/deleteReserva/{id}")
    public ResponseEntity<Void> eliminarReserva(@PathVariable Long id){
            repository.deleteById(id);
            return ResponseEntity.noContent().build();

    }

    @PutMapping("/editarReserva/{id}")
    public ResponseEntity<?> editarReserva(@PathVariable Long id, @RequestParam Long numHabitacion, @RequestBody ReservaModel reservaRequest) {
        return repository.findById(id).map(reserva -> {

            // Obtener la habitación anterior
            Optional<HabitacionModel> habitacionAnteriorOpt = habitacionRepository.findByNumHabitacion(reserva.getNumHabitacion());

            // Si cambia la habitación, actualizar la anterior a VACIA_LIMPIA
            if (!reserva.getNumHabitacion().equals(numHabitacion) && habitacionAnteriorOpt.isPresent()) {
                HabitacionModel habitacionAnterior = habitacionAnteriorOpt.get();
                if (habitacionAnterior.getEstado() == EstadoHabitacion.OCUPADA_LIMPIA) {
                    habitacionAnterior.setEstado(EstadoHabitacion.VACIA_LIMPIA);
                    habitacionRepository.save(habitacionAnterior);
                }
            }

            // Buscar la nueva habitación
            Optional<HabitacionModel> habitacionNuevaOpt = habitacionRepository.findByNumHabitacion(numHabitacion);
            if (habitacionNuevaOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("La nueva habitación no existe");
            }

            // Actualizar la reserva con los nuevos datos
            reserva.setNumHabitacion(numHabitacion);
            reserva.setTarifa(reservaRequest.getTarifa());
            reserva.setFechaEntrada(reservaRequest.getFechaEntrada());
            reserva.setFechaSalida(reservaRequest.getFechaSalida());
            reserva.setNumeroHuespedes(reservaRequest.getNumeroHuespedes());

            // Guardar la reserva actualizada
            repository.save(reserva);

            return ResponseEntity.ok(reserva);
        }).orElse(ResponseEntity.notFound().build());
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

            Optional<HabitacionModel> habitacion = habitacionRepository
                    .findByNumHabitacion(reservaRequest.getNumHabitacion());
            HabitacionModel habitacionModel = habitacion.get();
            if(habitacionModel.getEstado() == EstadoHabitacion.VACIA_LIMPIA){
                habitacionModel.setEstado(EstadoHabitacion.OCUPADA_LIMPIA);
                habitacionRepository.save(habitacionModel);

            }

            return ResponseEntity.ok(reservaRequest);
        }else
            return ResponseEntity.badRequest().build();
    }

    @PutMapping("/checkout/{id}")
    public ResponseEntity<ReservaModel> checkOut(@PathVariable Long id){
        // Buscar la reserva por ID
        Optional<ReservaModel> reservaOptional = repository.findById(id);

        if (!reservaOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        ReservaModel reserva = reservaOptional.get();

        // Verificar si la reserva está activa
        if (reserva.getEstatus() != EstatusReserva.CONFIRMADA) {
            return ResponseEntity.badRequest().body(reserva);
        }

        // Cambiar el estado de la reserva a COMPLETADA
        reserva.setEstatus(EstatusReserva.COMPLETADA);
        repository.save(reserva);

        // Buscar la habitación asociada a la reserva
        Optional<HabitacionModel> habitacionOptional = habitacionRepository.findByNumHabitacion(reserva.getNumHabitacion());

        if (habitacionOptional.isPresent()) {
            HabitacionModel habitacion = habitacionOptional.get();

            // Cambiar el estado de la habitación a VACIA_SUCIA después del check-out
            habitacion.setEstado(EstadoHabitacion.VACIA_SUCIA);
            habitacionRepository.save(habitacion);
        }

        return ResponseEntity.ok(reserva);
    }






}