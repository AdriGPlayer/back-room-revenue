package com.unideh.roomrevenue.roomrevenue.services;

import com.unideh.roomrevenue.roomrevenue.models.UserModel;
import com.unideh.roomrevenue.roomrevenue.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    @Autowired
    private UserRepository userRepository;

    public String getUserType(String user, String password){
        UserModel usermodel = userRepository.findByUserAndPassword(user, password);
        if (usermodel != null)
            return usermodel.getUserType().name();

        return null;
    }

}