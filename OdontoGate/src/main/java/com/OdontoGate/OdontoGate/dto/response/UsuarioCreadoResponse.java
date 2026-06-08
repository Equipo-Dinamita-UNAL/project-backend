package com.OdontoGate.OdontoGate.dto.response;
import com.OdontoGate.OdontoGate.model.UserType;

import lombok.Data;

@Data
public class UsuarioCreadoResponse {
    private Integer id;
    private String name;
    private String lastname;
    private String email;
    private String phone;
    private Boolean active;
    private UserType userType;
}