package com.OdontoGate.ArtefactoOdontoGate.service;

import com.OdontoGate.ArtefactoOdontoGate.dto.MedicalRecord.Request.MedicalRecordRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.MedicalRecord.Responses.MedicalRecordResponse;
import java.util.List;

/**
 * Define el contrato publico de MedicalRecordService.
 */
public interface MedicalRecordService {

    /**
     * Ejecuta la operacion publica create.
     */
    MedicalRecordResponse create(MedicalRecordRequest request);

    /**
     * Ejecuta la operacion publica update.
     */
    MedicalRecordResponse update(Integer id, MedicalRecordRequest request);

    /**
     * Ejecuta la operacion publica findById.
     */
    MedicalRecordResponse findById(Integer id);

    /**
     * Ejecuta la operacion publica findByPatient.
     */
    List<MedicalRecordResponse> findByPatient(Integer patientId);

    /**
     * Ejecuta la operacion publica delete.
     */
    void delete(Integer id);
}