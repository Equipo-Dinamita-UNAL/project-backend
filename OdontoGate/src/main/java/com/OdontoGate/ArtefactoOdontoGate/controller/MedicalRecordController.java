package com.OdontoGate.ArtefactoOdontoGate.controller;

import com.OdontoGate.ArtefactoOdontoGate.dto.MedicalRecord.Request.MedicalRecordRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.MedicalRecord.Responses.MedicalRecordResponse;
import com.OdontoGate.ArtefactoOdontoGate.service.MedicalRecordService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * Define el contrato publico de MedicalRecordController.
 */
@RestController
@RequestMapping("/api/medical-records")
public class MedicalRecordController {

    private final MedicalRecordService service;

    /**
     * Ejecuta la operacion publica MedicalRecordController.
     */
    public MedicalRecordController(MedicalRecordService service) {
        this.service = service;
    }

    /**
     * Ejecuta la operacion publica create.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MedicalRecordResponse create(
            @RequestBody MedicalRecordRequest request) {
        return service.create(request);
    }

    /**
     * Ejecuta la operacion publica update.
     */
    @PutMapping("/{id}")
    public MedicalRecordResponse update(
            @PathVariable Integer id,
            @RequestBody MedicalRecordRequest request) {
        return service.update(id, request);
    }

    /**
     * Ejecuta la operacion publica findById.
     */
    @GetMapping("/{id}")
    public MedicalRecordResponse findById(@PathVariable Integer id) {
        return service.findById(id);
    }

    /**
     * Ejecuta la operacion publica findByPatient.
     */
    @GetMapping("/patient/{patientId}")
    public List<MedicalRecordResponse> findByPatient(
            @PathVariable Integer patientId) {
        return service.findByPatient(patientId);
    }

    /**
     * Ejecuta la operacion publica delete.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}