package com.OdontoGate.ArtefactoOdontoGate.service;

import com.OdontoGate.ArtefactoOdontoGate.dto.MedicalRecord.Request.MedicalRecordRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.MedicalRecord.Responses.MedicalRecordResponse;
import com.OdontoGate.ArtefactoOdontoGate.model.MedicalRecord;
import com.OdontoGate.ArtefactoOdontoGate.model.Patient;
import com.OdontoGate.ArtefactoOdontoGate.exception.MedicalRecordExceptions;
import com.OdontoGate.ArtefactoOdontoGate.model.MedicalRecord;
import com.OdontoGate.ArtefactoOdontoGate.repository.MedicalRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import com.OdontoGate.ArtefactoOdontoGate.repository.PatientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordRepository repository;

    private final PdfService pdfService;

    private final PatientRepository patientRepository;
        

    @Override
    public MedicalRecordResponse create(MedicalRecordRequest request) {

        // Verificar que el paciente existe y está activo antes de registrar
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Paciente no encontrado con ID: " + request.getPatientId()
                ));

        if (!Boolean.TRUE.equals(patient.getActive())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "No se puede registrar una evolución clínica: el paciente está inactivo."
            );
        }

        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setPatientId(request.getPatientId());
        medicalRecord.setDiagnosis(request.getDiagnosis());
        medicalRecord.setTreatment(request.getTreatment());
        medicalRecord.setObservations(request.getObservations());
        medicalRecord.setDate(request.getDate());

        return toResponse(repository.save(medicalRecord));
    }

    @Override
    public MedicalRecordResponse update(Integer id, MedicalRecordRequest request) {
        MedicalRecord medicalRecord = repository.findById(id)
                .orElseThrow(() -> new MedicalRecordExceptions.NotFoundException(id));
        medicalRecord.setDiagnosis(request.getDiagnosis());
        medicalRecord.setTreatment(request.getTreatment());
        medicalRecord.setObservations(request.getObservations());
        medicalRecord.setDate(request.getDate());
        return toResponse(repository.save(medicalRecord));
    }

    @Override
    public MedicalRecordResponse findById(Integer id) {
        MedicalRecord medicalRecord = repository.findById(id)
                .orElseThrow(() -> new MedicalRecordExceptions.NotFoundException(id));
        return toResponse(medicalRecord);
    }

    @Override
    public List<MedicalRecordResponse> findByPatient(Integer patientId) {
        return repository.findByPatientId(patientId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new MedicalRecordExceptions.NotFoundException(id);
        }
        repository.deleteById(id);
    }

    @Override
    public byte[] getMedicalRecordPdfBytes(Integer id) throws Exception {
        MedicalRecordResponse record = this.findById(id);

        Map<String, Object> data = new HashMap<>();
        data.put("record", record);

        return pdfService.generatePdf("medical-record", data);
    }

    private MedicalRecordResponse toResponse(MedicalRecord medicalRecord) {
        MedicalRecordResponse response = new MedicalRecordResponse();
        response.setId(medicalRecord.getId());
        response.setPatientId(medicalRecord.getPatientId());
        response.setDiagnosis(medicalRecord.getDiagnosis());
        response.setTreatment(medicalRecord.getTreatment());
        response.setObservations(medicalRecord.getObservations());
        response.setDate(medicalRecord.getDate());
        response.setCreatedAt(medicalRecord.getCreatedAt());

        // Enriquecer la respuesta con el nombre del paciente
        patientRepository.findById(medicalRecord.getPatientId()).ifPresent(p ->
                response.setPatientName(p.getName() + " " + p.getLastname())
        );

        return response;
    }
}