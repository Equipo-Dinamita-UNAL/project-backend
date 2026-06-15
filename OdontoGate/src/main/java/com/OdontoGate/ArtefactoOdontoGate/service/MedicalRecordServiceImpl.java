package com.OdontoGate.ArtefactoOdontoGate.service;

import com.OdontoGate.ArtefactoOdontoGate.dto.MedicalRecord.Request.MedicalRecordRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.MedicalRecord.Responses.MedicalRecordResponse;
import com.OdontoGate.ArtefactoOdontoGate.model.MedicalRecord;
import com.OdontoGate.ArtefactoOdontoGate.exception.MedicalRecordExceptions;
import com.OdontoGate.ArtefactoOdontoGate.repository.MedicalRecordRepository;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Define el contrato publico de MedicalRecordServiceImpl.
 */
@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordRepository repository;

    /**
     * Ejecuta la operacion publica MedicalRecordServiceImpl.
     */
    public MedicalRecordServiceImpl(MedicalRecordRepository repository) {
        this.repository = repository;
    }

    /**
     * Ejecuta la operacion publica create.
     */
    @Override
    public MedicalRecordResponse create(MedicalRecordRequest request) {
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setPatientId(request.getPatientId());
        medicalRecord.setDiagnosis(request.getDiagnosis());
        medicalRecord.setTreatment(request.getTreatment());
        medicalRecord.setObservations(request.getObservations());
        medicalRecord.setDate(request.getDate());
        return toResponse(repository.save(medicalRecord));
    }

    /**
     * Ejecuta la operacion publica update.
     */
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

    /**
     * Ejecuta la operacion publica findById.
     */
    @Override
    public MedicalRecordResponse findById(Integer id) {
        MedicalRecord medicalRecord = repository.findById(id)
                .orElseThrow(() -> new MedicalRecordExceptions.NotFoundException(id));
        return toResponse(medicalRecord);
    }

    /**
     * Ejecuta la operacion publica findByPatient.
     */
    @Override
    public List<MedicalRecordResponse> findByPatient(Integer patientId) {
        return repository.findByPatientId(patientId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Ejecuta la operacion publica delete.
     */
    @Override
    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new MedicalRecordExceptions.NotFoundException(id);
        }
        repository.deleteById(id);
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
        return response;
    }
}