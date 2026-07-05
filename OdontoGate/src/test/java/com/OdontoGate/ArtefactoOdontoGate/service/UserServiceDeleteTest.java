package com.OdontoGate.ArtefactoOdontoGate.service;

import com.OdontoGate.ArtefactoOdontoGate.dto.Login.requests.DeleteUserRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.Login.responses.DeleteUserResponse;
import com.OdontoGate.ArtefactoOdontoGate.model.User;
import com.OdontoGate.ArtefactoOdontoGate.repository.AdministratorRepository;
import com.OdontoGate.ArtefactoOdontoGate.repository.DoctorRepository;
import com.OdontoGate.ArtefactoOdontoGate.repository.PatientRepository;
import com.OdontoGate.ArtefactoOdontoGate.repository.RoleRepository;
import com.OdontoGate.ArtefactoOdontoGate.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceDeleteTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AdministratorRepository administratorRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void deleteUser_DeberiaDesactivarDoctor_CuandoCorreoExiste() {
        DeleteUserRequest request = new DeleteUserRequest();
        request.setEmail("doctor@test.com");

        User user = new User();
        user.setId(1);
        user.setEmail("doctor@test.com");
        user.setActive(true);

        when(userRepository.findByEmail("doctor@test.com")).thenReturn(user);

        DeleteUserResponse response = userService.deleteUser(request);

        assertNotNull(response);
        assertEquals("Usuario desactivado correctamente.", response.getMensaje());
        assertEquals("doctor@test.com", response.getEmail());

        verify(userRepository, times(1)).save(user);
        verify(doctorRepository, never()).deleteById(1);
        verify(patientRepository, never()).deleteById(1);
        verify(administratorRepository, never()).deleteById(1);
        verify(userRepository, never()).deleteById(1);
    }

    @Test
    void deleteUser_DeberiaDesactivarPaciente_CuandoCorreoExiste() {
        DeleteUserRequest request = new DeleteUserRequest();
        request.setEmail("patient@test.com");

        User user = new User();
        user.setId(2);
        user.setEmail("patient@test.com");
        user.setActive(true);

        when(userRepository.findByEmail("patient@test.com")).thenReturn(user);

        DeleteUserResponse response = userService.deleteUser(request);

        assertNotNull(response);
        assertEquals("Usuario desactivado correctamente.", response.getMensaje());
        assertEquals("patient@test.com", response.getEmail());

        verify(userRepository, times(1)).save(user);
        verify(patientRepository, never()).deleteById(2);
        verify(doctorRepository, never()).deleteById(2);
        verify(administratorRepository, never()).deleteById(2);
        verify(userRepository, never()).deleteById(2);
    }

    @Test
    void deleteUser_DeberiaLanzarNotFound_CuandoCorreoNoExiste() {
        DeleteUserRequest request = new DeleteUserRequest();
        request.setEmail("noexiste@test.com");

        when(userRepository.findByEmail("noexiste@test.com")).thenReturn(null);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> userService.deleteUser(request)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Correo no asociado con ningún usuario.", exception.getReason());

        verify(userRepository, never()).deleteById(anyInt());
        verify(doctorRepository, never()).deleteById(anyInt());
        verify(patientRepository, never()).deleteById(anyInt());
        verify(administratorRepository, never()).deleteById(anyInt());
        verify(userRepository, never()).save(any(User.class));
    }
}
