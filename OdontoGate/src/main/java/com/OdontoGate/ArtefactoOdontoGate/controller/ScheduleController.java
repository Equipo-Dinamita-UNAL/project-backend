package com.OdontoGate.ArtefactoOdontoGate.controller;

import com.OdontoGate.ArtefactoOdontoGate.dto.request.ScheduleRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.response.ScheduleResponse;
import com.OdontoGate.ArtefactoOdontoGate.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

/**
 * Define el contrato publico de ScheduleController.
 */
@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    // GET /api/schedules
    /**
     * Ejecuta la operacion publica getAll.
     */
    @GetMapping
    public ResponseEntity<List<ScheduleResponse>> getAll() {
        return ResponseEntity.ok(scheduleService.getAll());
    }

    // GET /api/schedules/doctor/1
    /**
     * Ejecuta la operacion publica getByDoctor.
     */
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<ScheduleResponse>> getByDoctor(@PathVariable Integer doctorId) {
        return ResponseEntity.ok(scheduleService.getByDoctor(doctorId));
    }

    // POST /api/schedules
    /**
     * Ejecuta la operacion publica create.
     */
    @PostMapping
    public ResponseEntity<ScheduleResponse> create(@RequestBody ScheduleRequest request) {
        return ResponseEntity.ok(scheduleService.create(request));
    }

    // DELETE /api/schedules/1
    /**
     * Ejecuta la operacion publica delete.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        scheduleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // PUT /api/schedules/1
    /**
     * Ejecuta la operacion publica update.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ScheduleResponse> update(
            @PathVariable Integer id,
            @RequestBody ScheduleRequest request) {
        return ResponseEntity.ok(scheduleService.update(id, request));
    }

    // PATCH /api/schedules/1/available?value=false
    /**
     * Ejecuta la operacion publica setAvailable.
     */
    @PatchMapping("/{id}/available")
    public ResponseEntity<ScheduleResponse> setAvailable(
            @PathVariable Integer id,
            @RequestParam Boolean value) {
        return ResponseEntity.ok(scheduleService.setAvailable(id, value));
    }
}