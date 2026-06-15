package com.OdontoGate.ArtefactoOdontoGate.dto.request;

import com.OdontoGate.ArtefactoOdontoGate.dto.validation.OnCreate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.time.LocalTime;

@Data
public class ScheduleRequest {

    @NotNull(groups = OnCreate.class)
    @Positive
    private Integer doctorId;

    @NotBlank(groups = OnCreate.class)
    private String weekday;

    @NotNull(groups = OnCreate.class)
    private LocalTime startTime;

    @NotNull(groups = OnCreate.class)
    private LocalTime endTime;

    @NotNull(groups = OnCreate.class)
    private Boolean isAvailable;
}
