package com.unideh.roomrevenue.roomrevenue.controllers;

import com.unideh.roomrevenue.roomrevenue.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginService loginService;
    @PostMapping
    public Map<String, Object> login(@RequestBody Map<String, String> credentials) {
        String user = credentials.get("user");
        String password = credentials.get("password");

        String userType = loginService.getUserType(user, password);

        Map<String, Object> response = new HashMap<>();
        if (userType != null) {
            response.put("success", true);
            response.put("userType", userType);
        } else {
            response.put("success", false);
            response.put("message", "Credenciales incorrectas");
        }

        return response;
    }

}
