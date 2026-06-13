package com.OdontoGate.ArtefactoOdontoGate.controller;

import com.OdontoGate.ArtefactoOdontoGate.dto.request.ScheduleRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.response.ScheduleResponse;
import com.OdontoGate.ArtefactoOdontoGate.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<ScheduleResponse> create(@RequestBody ScheduleRequest request) {
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
            @RequestBody ScheduleRequest request) {
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