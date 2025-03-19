package com.unideh.roomrevenue.roomrevenue.services;

import com.unideh.roomrevenue.roomrevenue.models.UserModel;
import com.unideh.roomrevenue.roomrevenue.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EncryptionService encryptionService;

    public String getUserType(String user, String password) {
        String encryptedPassword = encryptionService.encryptPassword(password); // Encripta antes de comparar
        UserModel usermodel = userRepository.findByUserAndPassword(user, encryptedPassword);

        return (usermodel != null) ? usermodel.getUserType().name() : null;
    }
}