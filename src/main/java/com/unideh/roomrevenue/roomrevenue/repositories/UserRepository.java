package com.unideh.roomrevenue.roomrevenue.repositories;

import com.unideh.roomrevenue.roomrevenue.models.UserModel;
import com.unideh.roomrevenue.roomrevenue.models.UserType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<UserModel, Long> {
    UserModel findByUserAndPassword(String user, String password);
    List<UserModel> findByUserType(UserType userType);
}
