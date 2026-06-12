package com.OdontoGate.ArtefactoOdontoGate.service.impl;

import com.OdontoGate.ArtefactoOdontoGate.dto.request.MedicalRecordRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.response.MedicalRecordResponse;
import com.OdontoGate.ArtefactoOdontoGate.entity.MedicalRecord;
import com.OdontoGate.ArtefactoOdontoGate.exception.MedicalRecordNotFoundException;
import com.OdontoGate.ArtefactoOdontoGate.repository.MedicalRecordRepository;
import com.OdontoGate.ArtefactoOdontoGate.service.MedicalRecordService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordRepository repository;

    public MedicalRecordServiceImpl(MedicalRecordRepository repository) {
        this.repository = repository;
    }

    @Override
    public MedicalRecordResponse create(MedicalRecordRequest request) {
        MedicalRecord record = new MedicalRecord();
        record.setPatientId(request.getPatientId());
        record.setDiagnosis(request.getDiagnosis());
        record.setTreatment(request.getTreatment());
        record.setObservations(request.getObservations());
        record.setDate(request.getDate());
        return toResponse(repository.save(record));
    }

    @Override
    public MedicalRecordResponse update(Integer id, MedicalRecordRequest request) {
        MedicalRecord record = repository.findById(id)
                .orElseThrow(() -> new MedicalRecordNotFoundException(id));
        record.setDiagnosis(request.getDiagnosis());
        record.setTreatment(request.getTreatment());
        record.setObservations(request.getObservations());
        record.setDate(request.getDate());
        return toResponse(repository.save(record));
    }

    @Override
    public MedicalRecordResponse findById(Integer id) {
        MedicalRecord record = repository.findById(id)
                .orElseThrow(() -> new MedicalRecordNotFoundException(id));
        return toResponse(record);
    }

    @Override
    public List<MedicalRecordResponse> findByPatient(Integer patientId) {
        return repository.findByPatientId(patientId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new MedicalRecordNotFoundException(id);
        }
        repository.deleteById(id);
    }

    private MedicalRecordResponse toResponse(MedicalRecord record) {
        MedicalRecordResponse response = new MedicalRecordResponse();
        response.setId(record.getId());
        response.setPatientId(record.getPatientId());
        response.setDiagnosis(record.getDiagnosis());
        response.setTreatment(record.getTreatment());
        response.setObservations(record.getObservations());
        response.setDate(record.getDate());
        response.setCreatedAt(record.getCreatedAt());
        return response;
    }
}