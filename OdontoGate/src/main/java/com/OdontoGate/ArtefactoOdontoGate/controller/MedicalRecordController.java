package com.OdontoGate.ArtefactoOdontoGate.controller;

import com.OdontoGate.ArtefactoOdontoGate.dto.MedicalRecord.Request.MedicalRecordRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.MedicalRecord.Responses.MedicalRecordResponse;
import com.OdontoGate.ArtefactoOdontoGate.dto.validation.OnCreate;
import com.OdontoGate.ArtefactoOdontoGate.exception.MedicalRecordExceptions;
import com.OdontoGate.ArtefactoOdontoGate.model.User;
import com.OdontoGate.ArtefactoOdontoGate.repository.UserRepository;
import com.OdontoGate.ArtefactoOdontoGate.service.MedicalRecordService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

import java.util.List;

@RestController
@RequestMapping("/api/medical-records")
public class MedicalRecordController {

    private final MedicalRecordService service;
    private final UserRepository userRepository;

    public MedicalRecordController(MedicalRecordService service,
                                   UserRepository userRepository) {
        this.service = service;
        this.userRepository = userRepository;
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
    @PreAuthorize("hasRole('ADMINISTRATOR') "
            + "or hasAuthority('DOCTOR_LEER_HISTORIA_CLINICA') "
            + "or hasAuthority('PATIENT_LEER_CITA')")
    public MedicalRecordResponse findById(@PathVariable Integer id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isPatient = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("PATIENT_LEER_CITA"));

        MedicalRecordResponse record = service.findById(id);

        if (isPatient) {
            Integer requestingUserId = getCurrentUserId(auth);
            if (!record.getPatientId().equals(requestingUserId)) {
                throw new MedicalRecordExceptions.PatientNotOwnerException();
            }
        }
        return record;
    }

    @GetMapping("/patient/{patientId}")
<<<<<<< HEAD
    @PreAuthorize("hasRole('ADMINISTRATOR') "
            + "or hasAuthority('DOCTOR_LEER_HISTORIA_CLINICA') "
            + "or hasAuthority('PATIENT_LEER_CITA')")
    public List<MedicalRecordResponse> findByPatient(@PathVariable Integer patientId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isPatient = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("PATIENT_LEER_CITA"));

        if (isPatient) {
            Integer requestingUserId = getCurrentUserId(auth);
            if (!requestingUserId.equals(patientId)) {
                throw new MedicalRecordExceptions.PatientNotOwnerException();
            }
        }
=======
    @PreAuthorize(
            "hasRole('ADMINISTRATOR') " +
                    "or hasAuthority('DOCTOR_LEER_HISTORIA_CLINICA') " +
                    "or (hasRole('PATIENT') and #patientId == authentication.principal.id)"
    )
    public List<MedicalRecordResponse> findByPatient(
            @PathVariable Integer patientId) {
>>>>>>> 7942561dfdf7b47854347f4d793fea3e1b875aba
        return service.findByPatient(patientId);
    }

    @GetMapping("/{id}/pdf")
    @PreAuthorize("hasRole('ADMINISTRATOR') "
            + "or hasAuthority('DOCTOR_LEER_HISTORIA_CLINICA') "
            + "or hasAuthority('PATIENT_LEER_CITA')")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Integer id) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            boolean isPatient = auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("PATIENT_LEER_CITA"));

            MedicalRecordResponse record = service.findById(id);

            if (isPatient) {
                Integer requestingUserId = getCurrentUserId(auth);
                if (!record.getPatientId().equals(requestingUserId)) {
                    throw new MedicalRecordExceptions.PatientNotOwnerException();
                }
            }

            byte[] pdfBytes = service.getMedicalRecordPdfBytes(id);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", "historia-clinica-" + id + ".pdf");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (MedicalRecordExceptions.PatientNotOwnerException ex) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }

    private Integer getCurrentUserId(Authentication auth) {
        String email = auth.getName();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new IllegalStateException("Usuario no encontrado");
        }
        return user.getId();
    }
}