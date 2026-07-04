package com.OdontoGate.ArtefactoOdontoGate.controller;

import com.OdontoGate.ArtefactoOdontoGate.dto.MedicalRecord.Request.MedicalRecordRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.MedicalRecord.Responses.MedicalRecordResponse;
import com.OdontoGate.ArtefactoOdontoGate.dto.validation.OnCreate;
import com.OdontoGate.ArtefactoOdontoGate.service.MedicalRecordService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/medical-records")
public class MedicalRecordController {

    private final MedicalRecordService service;

        public MedicalRecordController(MedicalRecordService service) {
        this.service = service;
    }

        @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasAuthority('DOCTOR_CREAR_HISTORIA_CLINICA')")
    public MedicalRecordResponse create(
            @Validated(OnCreate.class) @RequestBody MedicalRecordRequest request) {
        return service.create(request);
    }

        @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasAuthority('DOCTOR_MODIFICAR_HISTORIA_CLINICA')")
    public MedicalRecordResponse update(
            @PathVariable Integer id,
            @Valid @RequestBody MedicalRecordRequest request) {
        return service.update(id, request);
    }

        @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasAuthority('DOCTOR_LEER_HISTORIA_CLINICA')")
    public MedicalRecordResponse findById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize(
            "hasRole('ADMINISTRATOR') " +
                    "or hasAuthority('DOCTOR_LEER_HISTORIA_CLINICA') " +
                    "or (hasRole('PATIENT') and #patientId == authentication.principal.id)"
    )
    public List<MedicalRecordResponse> findByPatient(
            @PathVariable Integer patientId) {
        return service.findByPatient(patientId);
    }
      @GetMapping("/{id}/pdf")
    @PreAuthorize( "hasRole('ADMINISTRATOR') " +
    "or hasAuthority('DOCTOR_LEER_HISTORIA_CLINICA') " +
    "or hasRole('PATIENT')")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Integer id) throws Exception {

        byte[] pdfBytes = service.getMedicalRecordPdfBytes(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("inline", "historia-clinica-" + id + ".pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

        @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}