package com.OdontoGate.ArtefactoOdontoGate.dto.response;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class AdministratorSummaryResponse {

    private Integer id;
    private String name;
    private String lastname;
    private String email;
    private String phone;
    private Boolean active;
    private LocalDateTime createdAt;
    private String position;
}
