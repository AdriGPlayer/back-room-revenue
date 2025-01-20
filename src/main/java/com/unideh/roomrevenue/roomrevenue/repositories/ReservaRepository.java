package com.unideh.roomrevenue.roomrevenue.repositories;

import com.unideh.roomrevenue.roomrevenue.models.ReservaModel;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ReservaRepository extends CrudRepository<ReservaModel, Long> {

}
