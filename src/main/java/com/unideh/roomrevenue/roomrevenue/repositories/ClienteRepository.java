package com.unideh.roomrevenue.roomrevenue.repositories;

import com.unideh.roomrevenue.roomrevenue.models.ClientModel;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ClienteRepository extends CrudRepository<ClientModel, Long> {
    Optional<ClientModel> findByEmail(String email);

}
