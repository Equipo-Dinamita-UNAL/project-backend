package com.OdontoGate.ArtefactoOdontoGate.service;

import com.OdontoGate.ArtefactoOdontoGate.dto.MedicalRecord.Request.MedicalRecordRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.MedicalRecord.Responses.MedicalRecordResponse;
import com.OdontoGate.ArtefactoOdontoGate.exception.MedicalRecordExceptions.NotFoundException;
import com.OdontoGate.ArtefactoOdontoGate.model.MedicalRecord;
import com.OdontoGate.ArtefactoOdontoGate.model.Patient;
import com.OdontoGate.ArtefactoOdontoGate.repository.MedicalRecordRepository;
import com.OdontoGate.ArtefactoOdontoGate.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MedicalRecordServiceTest {

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PdfService pdfService;

    @InjectMocks
    private MedicalRecordServiceImpl medicalRecordService;

    private static final Integer EXISTING_ID = 1;
    private static final Integer MISSING_ID = 99;
    private static final Integer PATIENT_ID = 10;

    private MedicalRecordRequest validRequest;

    @BeforeEach
    void setUp() {
        validRequest = new MedicalRecordRequest();
        validRequest.setPatientId(PATIENT_ID);
        validRequest.setDiagnosis("Caries dental");
        validRequest.setTreatment("Resina compuesta");
        validRequest.setObservations("Control en 6 meses");
        validRequest.setDate(LocalDateTime.now());
    }

    @Test
    @DisplayName("Crea historia clínica y mapea correctamente todos los campos")
    void cuandoDatosSonValidos_debeMapearYRetornarResponse() {
        Patient patient = new Patient();
        patient.setId(PATIENT_ID);
        patient.setName("Ana");
        patient.setLastname("Perez");
        patient.setActive(true);

        when(patientRepository.findById(PATIENT_ID)).thenReturn(Optional.of(patient));
        when(medicalRecordRepository.save(any(MedicalRecord.class)))
                .thenAnswer(invocation -> {
                    MedicalRecord saved = invocation.getArgument(0);
                    saved.setId(EXISTING_ID);
                    return saved;
                });

        MedicalRecordResponse response = medicalRecordService.create(validRequest);

        assertNotNull(response);
        assertEquals(EXISTING_ID, response.getId());
        assertEquals(PATIENT_ID, response.getPatientId());
        assertEquals("Caries dental", response.getDiagnosis());
        assertEquals("Resina compuesta", response.getTreatment());
        assertEquals("Control en 6 meses", response.getObservations());
        verify(medicalRecordRepository, times(1)).save(any(MedicalRecord.class));
    }

    @Test
    @DisplayName("Lanza excepción al buscar una historia clínica inexistente")
    void cuandoHistoriaClinicaNoExiste_debeLanzarExcepcion() {
        when(medicalRecordRepository.findById(MISSING_ID))
                .thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> medicalRecordService.findById(MISSING_ID));

        assertEquals("Historia clínica no encontrada con id: " + MISSING_ID,
                ex.getMessage());
    }

    @Test
    @DisplayName("No elimina y lanza excepción cuando la historia clínica no existe")
    void cuandoSeEliminaHistoriaInexistente_debeLanzarExcepcionYNoEliminar() {
        when(medicalRecordRepository.existsById(MISSING_ID))
                .thenReturn(false);

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> medicalRecordService.delete(MISSING_ID));

        assertEquals("Historia clínica no encontrada con id: " + MISSING_ID,
                ex.getMessage());
        verify(medicalRecordRepository, never()).deleteById(MISSING_ID);
    }
}
