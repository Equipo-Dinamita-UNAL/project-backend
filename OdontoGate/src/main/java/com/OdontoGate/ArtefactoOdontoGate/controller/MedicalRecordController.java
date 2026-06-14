package com.OdontoGate.ArtefactoOdontoGate.controller;

import com.OdontoGate.ArtefactoOdontoGate.dto.MedicalRecord.Request.MedicalRecordRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.MedicalRecord.Responses.MedicalRecordResponse;
import com.OdontoGate.ArtefactoOdontoGate.service.MedicalRecordService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/medical-records")
public class MedicalRecordController {

    private final MedicalRecordService service;

    public MedicalRecordController(MedicalRecordService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MedicalRecordResponse create(
            @RequestBody MedicalRecordRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public MedicalRecordResponse update(
            @PathVariable Integer id,
            @RequestBody MedicalRecordRequest request) {
        return service.update(id, request);
    }

    @GetMapping("/{id}")
    public MedicalRecordResponse findById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @GetMapping("/patient/{patientId}")
    public List<MedicalRecordResponse> findByPatient(
            @PathVariable Integer patientId) {
        return service.findByPatient(patientId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}