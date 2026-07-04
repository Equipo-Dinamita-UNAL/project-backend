package com.OdontoGate.ArtefactoOdontoGate.service;

import com.OdontoGate.ArtefactoOdontoGate.dto.MedicalRecord.Request.MedicalRecordRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.MedicalRecord.Responses.MedicalRecordResponse;

import java.util.List;

public interface MedicalRecordService {

    MedicalRecordResponse create(MedicalRecordRequest request);

    MedicalRecordResponse update(Integer id, MedicalRecordRequest request);

    MedicalRecordResponse findById(Integer id);

    List<MedicalRecordResponse> findByPatient(Integer patientId);

    void delete(Integer id);

    byte[] getMedicalRecordPdfBytes(Integer id) throws Exception;
}