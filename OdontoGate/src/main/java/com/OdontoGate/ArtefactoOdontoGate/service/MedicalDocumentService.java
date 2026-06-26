package com.OdontoGate.ArtefactoOdontoGate.service;

import com.OdontoGate.ArtefactoOdontoGate.dto.MedicalRecord.Responses.MedicalDocumentResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MedicalDocumentService {

    /**
     * Sube un archivo médico asociándolo al paciente y opcionalmente a una historia clínica.
     *
     * @param file            Archivo a subir
     * @param patientId       ID del paciente dueño del documento
     * @param medicalRecordId ID de la historia clínica (puede ser null)
     * @param description     Descripción opcional del documento
     * @param uploadedBy      ID del usuario que sube el archivo (doctor/admin)
     * @return Respuesta con los metadatos del documento guardado
     */
    MedicalDocumentResponse upload(MultipartFile file,
                                   Integer patientId,
                                   Integer medicalRecordId,
                                   String description,
                                   Integer uploadedBy);


    List<MedicalDocumentResponse> findByPatient(Integer patientId,
                                                Integer requestingUserId,
                                                String requestingUserRole);


    List<MedicalDocumentResponse> findByMedicalRecord(Integer medicalRecordId);


    MedicalDocumentResponse findById(Integer id,
                                     Integer requestingUserId,
                                     String requestingUserRole);

 
    void delete(Integer id);

    String getFilePath(Integer id,
                       Integer requestingUserId,
                       String requestingUserRole);
}