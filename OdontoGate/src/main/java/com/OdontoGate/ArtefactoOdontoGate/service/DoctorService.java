package com.OdontoGate.ArtefactoOdontoGate.service;

import com.OdontoGate.ArtefactoOdontoGate.dto.response.AppointmentResponse;
import com.OdontoGate.ArtefactoOdontoGate.dto.response.DoctorPatientResponse;
import com.OdontoGate.ArtefactoOdontoGate.dto.response.DoctorSummaryResponse;
import com.OdontoGate.ArtefactoOdontoGate.model.Appointment;
import com.OdontoGate.ArtefactoOdontoGate.model.Doctor;
import com.OdontoGate.ArtefactoOdontoGate.model.Patient;
import com.OdontoGate.ArtefactoOdontoGate.repository.AppointmentRepository;
import com.OdontoGate.ArtefactoOdontoGate.repository.DoctorRepository;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;

    public List<DoctorSummaryResponse> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .filter(d -> Boolean.TRUE.equals(d.getActive()))
                .map(this::toDoctorSummaryResponse)
                .toList();
    }

    public List<DoctorPatientResponse> getScheduledPatients(Integer doctorId) {
        if (!doctorRepository.existsById(doctorId)) {
            throw new RuntimeException("Doctor no encontrado");
        }

        Map<Integer, DoctorPatientResponse> patientsById = new LinkedHashMap<>();

        appointmentRepository.findScheduledAppointmentsByDoctorId(doctorId)
                .forEach(appointment -> {
                    Patient patient = appointment.getPatient();
                    DoctorPatientResponse patientResponse = patientsById.computeIfAbsent(
                            patient.getId(),
                            id -> toDoctorPatientResponse(patient)
                    );
                    patientResponse.getAppointments().add(toAppointmentResponse(appointment));
                });

        return new ArrayList<>(patientsById.values());
    }

    private DoctorSummaryResponse toDoctorSummaryResponse(Doctor doctor) {
        DoctorSummaryResponse response = new DoctorSummaryResponse();
        response.setId(doctor.getId());
        response.setName(doctor.getName());
        response.setLastname(doctor.getLastname());
        response.setEmail(doctor.getEmail());
        response.setPhone(doctor.getPhone());
        response.setActive(doctor.getActive());
        response.setCreatedAt(doctor.getCreatedAt());
        response.setSpeciality(doctor.getSpeciality());
        response.setMedicalLicense(doctor.getMedicalLicense());
        response.setPhotoUrl(doctor.getPhotoUrl());
        return response;
    }

    private DoctorPatientResponse toDoctorPatientResponse(Patient patient) {
        DoctorPatientResponse response = new DoctorPatientResponse();
        response.setId(patient.getId());
        response.setName(patient.getName());
        response.setLastname(patient.getLastname());
        response.setEmail(patient.getEmail());
        response.setPhone(patient.getPhone());
        response.setBirthDate(patient.getBirthDate());
        response.setBloodType(patient.getBloodType());
        response.setAllergies(patient.getAllergies());
        response.setAddress(patient.getAddress());
        response.setAppointments(new ArrayList<>());
        return response;
    }

    private AppointmentResponse toAppointmentResponse(Appointment appointment) {
        AppointmentResponse response = new AppointmentResponse();
        response.setId(appointment.getId());
        response.setDate(appointment.getDate());
        response.setTime(appointment.getTime());
        response.setStatus(appointment.getStatus());
        response.setReason(appointment.getReason());
        response.setPatientName(
                appointment.getPatient().getName() + " " +
                        appointment.getPatient().getLastname()
        );
        response.setDoctorName(
                appointment.getDoctor().getName() + " " +
                        appointment.getDoctor().getLastname()
        );
        return response;
    }
}