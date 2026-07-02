package com.OdontoGate.ArtefactoOdontoGate.service;

import com.OdontoGate.ArtefactoOdontoGate.dto.response.AdministratorSummaryResponse;
import com.OdontoGate.ArtefactoOdontoGate.dto.response.DoctorSummaryResponse;
import com.OdontoGate.ArtefactoOdontoGate.dto.response.PatientSummaryResponse;
import com.OdontoGate.ArtefactoOdontoGate.dto.response.RegisteredUsersResponse;
import com.OdontoGate.ArtefactoOdontoGate.model.Administrator;
import com.OdontoGate.ArtefactoOdontoGate.model.Doctor;
import com.OdontoGate.ArtefactoOdontoGate.model.Patient;
import com.OdontoGate.ArtefactoOdontoGate.repository.AdministratorRepository;
import com.OdontoGate.ArtefactoOdontoGate.repository.DoctorRepository;
import com.OdontoGate.ArtefactoOdontoGate.repository.PatientRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AdministratorRepository administratorRepository;

    public RegisteredUsersResponse getRegisteredPatientsAndDoctors() {
        List<PatientSummaryResponse> patients = patientRepository.findAll()
                .stream()
                .map(this::toPatientSummaryResponse)
                .toList();

        List<DoctorSummaryResponse> doctors = doctorRepository.findAll()
                .stream()
                .map(this::toDoctorSummaryResponse)
                .toList();

        List<AdministratorSummaryResponse> administrators = administratorRepository.findAll()
                .stream()
                .map(this::toAdministratorSummaryResponse)
                .toList();

        return new RegisteredUsersResponse(patients, doctors, administrators);
    }

    public RegisteredUsersResponse getActiveRegisteredPatientsAndDoctors() {
        List<PatientSummaryResponse> patients = patientRepository.findByActiveTrue()
                .stream()
                .map(this::toPatientSummaryResponse)
                .toList();

        List<DoctorSummaryResponse> doctors = doctorRepository.findByActiveTrue()
                .stream()
                .map(this::toDoctorSummaryResponse)
                .toList();

        List<AdministratorSummaryResponse> administrators = administratorRepository.findAll()
                .stream()
                .filter(a -> Boolean.TRUE.equals(a.getActive()))
                .map(this::toAdministratorSummaryResponse)
                .toList();

        return new RegisteredUsersResponse(patients, doctors, administrators);
    }

    public RegisteredUsersResponse getInactiveRegisteredPatientsAndDoctors() {
        List<PatientSummaryResponse> patients = patientRepository.findByActiveFalse()
                .stream()
                .map(this::toPatientSummaryResponse)
                .toList();

        List<DoctorSummaryResponse> doctors = doctorRepository.findByActiveFalse()
                .stream()
                .map(this::toDoctorSummaryResponse)
                .toList();

        List<AdministratorSummaryResponse> administrators = administratorRepository.findAll()
                .stream()
                .filter(a -> Boolean.FALSE.equals(a.getActive()))
                .map(this::toAdministratorSummaryResponse)
                .toList();

        return new RegisteredUsersResponse(patients, doctors, administrators);
    }

    private PatientSummaryResponse toPatientSummaryResponse(Patient patient) {
        PatientSummaryResponse response = new PatientSummaryResponse();
        response.setId(patient.getId());
        response.setName(patient.getName());
        response.setLastname(patient.getLastname());
        response.setEmail(patient.getEmail());
        response.setPhone(patient.getPhone());
        response.setActive(patient.getActive());
        response.setCreatedAt(patient.getCreatedAt());
        response.setBirthDate(patient.getBirthDate());
        response.setBloodType(patient.getBloodType());
        response.setAllergies(patient.getAllergies());
        response.setAddress(patient.getAddress());
        return response;
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

    private AdministratorSummaryResponse toAdministratorSummaryResponse(Administrator administrator) {
        AdministratorSummaryResponse response = new AdministratorSummaryResponse();
        response.setId(administrator.getId());
        response.setName(administrator.getName());
        response.setLastname(administrator.getLastname());
        response.setEmail(administrator.getEmail());
        response.setPhone(administrator.getPhone());
        response.setActive(administrator.getActive());
        response.setCreatedAt(administrator.getCreatedAt());
        response.setPosition(administrator.getPosition());
        return response;
    }
}