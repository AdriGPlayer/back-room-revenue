package com.unideh.roomrevenue.roomrevenue.DTO;

import com.unideh.roomrevenue.roomrevenue.models.ClientModel;
import com.unideh.roomrevenue.roomrevenue.models.ReservaModel;
import lombok.Data;

@Data
public class LoginRequest {
    private ClientModel cliente;
    private ReservaModel reserva;
}
