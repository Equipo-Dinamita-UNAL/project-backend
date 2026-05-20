package com.OdontoGate.ArtefactoOdontoGate.dto.Login.responses;
import lombok.Data;

@Data
public class UsuarioCreadoResponse {
    private Integer id;
    private String name;
    private String lastname;
    private String email;
    private String phone;
    private Boolean active;
}