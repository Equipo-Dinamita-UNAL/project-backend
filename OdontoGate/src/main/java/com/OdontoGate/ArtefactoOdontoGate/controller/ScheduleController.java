package com.OdontoGate.ArtefactoOdontoGate.controller;

import com.OdontoGate.ArtefactoOdontoGate.dto.request.ScheduleRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.response.ScheduleResponse;
import com.OdontoGate.ArtefactoOdontoGate.dto.validation.OnCreate;
import com.OdontoGate.ArtefactoOdontoGate.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    // GET /api/schedules
        @GetMapping
    public ResponseEntity<List<ScheduleResponse>> getAll() {
        return ResponseEntity.ok(scheduleService.getAll());
    }

    // GET /api/schedules/doctor/1
        @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<ScheduleResponse>> getByDoctor(@PathVariable Integer doctorId) {
        return ResponseEntity.ok(scheduleService.getByDoctor(doctorId));
    }

    // POST /api/schedules
        @PostMapping
    public ResponseEntity<ScheduleResponse> create(
            @Validated(OnCreate.class) @RequestBody ScheduleRequest request) {
        return ResponseEntity.ok(scheduleService.create(request));
    }

    // DELETE /api/schedules/1
        @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        scheduleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // PUT /api/schedules/1
        @PutMapping("/{id}")
    public ResponseEntity<ScheduleResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody ScheduleRequest request) {
        return ResponseEntity.ok(scheduleService.update(id, request));
    }

    // PATCH /api/schedules/1/available?value=false
        @PatchMapping("/{id}/available")
    public ResponseEntity<ScheduleResponse> setAvailable(
            @PathVariable Integer id,
            @RequestParam Boolean value) {
        return ResponseEntity.ok(scheduleService.setAvailable(id, value));
    }
}
