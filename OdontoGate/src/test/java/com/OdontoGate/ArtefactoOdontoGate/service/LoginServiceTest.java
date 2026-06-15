package com.OdontoGate.ArtefactoOdontoGate.service;

import com.OdontoGate.ArtefactoOdontoGate.dto.Login.requests.LoginRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.Login.responses.LoginResponse;
import com.OdontoGate.ArtefactoOdontoGate.model.User;
import com.OdontoGate.ArtefactoOdontoGate.model.UserType;
import com.OdontoGate.ArtefactoOdontoGate.repository.AdministratorRepository;
import com.OdontoGate.ArtefactoOdontoGate.repository.DoctorRepository;
import com.OdontoGate.ArtefactoOdontoGate.repository.PatientRepository;
import com.OdontoGate.ArtefactoOdontoGate.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AdministratorRepository administratorRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private LoginService loginService;

    @Test
    void login_DeberiaRetornarDoctor_CuandoCredencialesSonCorrectas() {
        LoginRequest request = new LoginRequest();
        request.setEmail("doctor@test.com");
        request.setPassword("123456");

        User user = new User();
        user.setId(1);
        user.setEmail("doctor@test.com");
        user.setPassword("123456");

        when(userRepository.findByEmailAndPassword("doctor@test.com", "123456"))
                .thenReturn(user);

        when(administratorRepository.existsById(1)).thenReturn(false);
        when(doctorRepository.existsById(1)).thenReturn(true);

        LoginResponse response = loginService.login(request);

        assertNotNull(response);
        assertEquals(UserType.DOCTOR, response.getUserType());

        verify(patientRepository, never()).existsById(1);
    }

    @Test
    void login_DeberiaRetornarPaciente_CuandoCredencialesSonCorrectas() {
        LoginRequest request = new LoginRequest();
        request.setEmail("patient@test.com");
        request.setPassword("123456");

        User user = new User();
        user.setId(2);
        user.setEmail("patient@test.com");
        user.setPassword("123456");

        when(userRepository.findByEmailAndPassword("patient@test.com", "123456"))
                .thenReturn(user);

        when(administratorRepository.existsById(2)).thenReturn(false);
        when(doctorRepository.existsById(2)).thenReturn(false);
        when(patientRepository.existsById(2)).thenReturn(true);

        LoginResponse response = loginService.login(request);

        assertNotNull(response);
        assertEquals(UserType.PATIENT, response.getUserType());
    }

    @Test
    void login_DeberiaLanzarBadRequest_CuandoCredencialesSonInvalidas() {
        LoginRequest request = new LoginRequest();
        request.setEmail("noexiste@test.com");
        request.setPassword("incorrecta");

        when(userRepository.findByEmailAndPassword("noexiste@test.com", "incorrecta"))
                .thenReturn(null);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> loginService.login(request)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Credenciales inválidas", exception.getReason());

        verify(administratorRepository, never()).existsById(anyInt());
        verify(doctorRepository, never()).existsById(anyInt());
        verify(patientRepository, never()).existsById(anyInt());
    }
}