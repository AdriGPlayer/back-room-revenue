package com.unideh.roomrevenue.roomrevenue.controllers;

import com.unideh.roomrevenue.roomrevenue.models.UserModel;
import com.unideh.roomrevenue.roomrevenue.models.UserType;
import com.unideh.roomrevenue.roomrevenue.repositories.UserRepository;
import com.unideh.roomrevenue.roomrevenue.services.EncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.lang.module.ResolutionException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository repository;
    @Autowired
    private EncryptionService encryptionService;

    // endpoint para guardar usuario
    @PostMapping("/postUser")
    public ResponseEntity<?> saveUser( @RequestBody UserModel userRequest){
        // Encriptamos contraseña
        String passwordEncrypt = encryptionService.encryptPassword(userRequest.getPassword());
        // Guardamos contraseña encriptada
        userRequest.setPassword(passwordEncrypt);
        // Guardamos usuario con contraseña encriptada
        UserModel user = repository.save(userRequest);
        return ResponseEntity.ok(user);
    }

    // endpoint para optener usuario por id
    @GetMapping("/getUser/{id}")
    public ResponseEntity<UserModel> getUserById(@PathVariable long id){
        UserModel user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("El  usuario con  id " +
                        id + " no existe"));
        return ResponseEntity.ok(user);
    }

    // endpoint para optener usuario por tipo
    @GetMapping("/getUserByType/{userType}")
    public ResponseEntity<List<UserModel>> getUserByType(@PathVariable UserType userType){
        List<UserModel> users = repository.findByUserType(userType);
        if (users.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(users);
    }
    // endpoint para eliminar usuario
    @DeleteMapping("/deleteUser/{id}")
    public  ResponseEntity<Map<String, Boolean>> deleteUserById(@PathVariable long id){
        UserModel user = repository.findById(id)
                .orElseThrow(()-> new ResolutionException("El usuario con id " +
                        id + " no existe"));
        repository.deleteById(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }

    // endpoint para actualizar
    @PutMapping("/updateUser/{id}")
    public ResponseEntity<?> updateUserById(@PathVariable long id, @RequestBody UserModel userRequest) {
        return repository.findById(id)
                .map(user -> {
                    if (userRequest.getUserType() == null) {
                        return ResponseEntity.badRequest().body("El tipo de usuario no puede ser nulo");
                    }
                    user.setName(userRequest.getName());
                    user.setLastname(userRequest.getLastname());
                    user.setEmail(userRequest.getEmail());
                    user.setUser(userRequest.getUser());
                    user.setUserType(userRequest.getUserType());
                    if (userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()) {
                        user.setPassword(encryptionService.encryptPassword(userRequest.getPassword()));
                    }
                    repository.save(user);
                    return ResponseEntity.ok(user);
                })
                .orElse(ResponseEntity.notFound().build());
    }



}
