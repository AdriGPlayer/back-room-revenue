package com.unideh.roomrevenue.roomrevenue.controllers;

import com.unideh.roomrevenue.roomrevenue.models.ClientModel;
import com.unideh.roomrevenue.roomrevenue.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/clientes")
@CrossOrigin(origins = "http://localhost:3000")
public class ClienteController {
    @Autowired
    private ClienteRepository repository;

    @GetMapping("/getCliente")
    public Iterable<ClientModel> getAllClientes() {
        return repository.findAll();
    }

    @GetMapping("/getClientePorEmail/{email}")
    public ResponseEntity<ClientModel> getClienteById(@PathVariable String email) {
        Optional<ClientModel> cliente = repository.findByEmail(email);
        return cliente.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/saveCliente")
    public ClientModel createCliente(@RequestBody ClientModel cliente) {
        return repository.save(cliente);
    }

    @PutMapping("/updateCliente/{id}")
    public ResponseEntity<ClientModel> updateCliente(@PathVariable Long id, @RequestBody ClientModel clienteDetails) {
        Optional<ClientModel> optionalCliente = repository.findById(id);
        if (optionalCliente.isPresent()) {
            ClientModel cliente = optionalCliente.get();
            cliente.setName(clienteDetails.getName());
            cliente.setLastname(clienteDetails.getLastname());
            cliente.setEmail(clienteDetails.getEmail());

            return ResponseEntity.ok(repository.save(cliente));
        } else {
            return ResponseEntity.notFound().build();
        }

    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCliente(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Buscar un cliente por email
    /*
    @GetMapping("/getClienteByEmail/{email}")
    public ResponseEntity<ClientModel> getClienteByEmail(@PathVariable String email) {
            ClientModel clientModel = repository.findByEmail(email);
        // Verificar si se encontró el cliente
        if (clientModel != null) {
            // Si se encuentra, devolver una respuesta con el cliente y el estado 200 (OK)
            return ResponseEntity.ok(clientModel);
        } else {
            // Si no se encuentra, devolver una respuesta con el estado 404 (Not Found)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        }*/

    }




