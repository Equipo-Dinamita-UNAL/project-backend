package com.OdontoGate.ArtefactoOdontoGate.service;

import com.OdontoGate.ArtefactoOdontoGate.dto.Login.requests.CrearUsuarioRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.Login.responses.UsuarioCreadoResponse;
import com.OdontoGate.ArtefactoOdontoGate.model.Role;
import com.OdontoGate.ArtefactoOdontoGate.model.User;
import com.OdontoGate.ArtefactoOdontoGate.model.UserType;
import com.OdontoGate.ArtefactoOdontoGate.repository.AdministratorRepository;
import com.OdontoGate.ArtefactoOdontoGate.repository.DoctorRepository;
import com.OdontoGate.ArtefactoOdontoGate.repository.PatientRepository;
import com.OdontoGate.ArtefactoOdontoGate.repository.RoleRepository;
import com.OdontoGate.ArtefactoOdontoGate.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceCreateTest {

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

    @InjectMocks
    private UserService userService;

    @Test
    void createUser_DeberiaCrearDoctor_CuandoDatosSonValidos() {
        CrearUsuarioRequest request = new CrearUsuarioRequest();
        request.setName("Carlos");
        request.setLastname("Perez");
        request.setEmail("doctor@test.com");
        request.setPassword("123456");
        request.setPhone("3001234567");
        request.setUserType(UserType.DOCTOR);
        request.setSpecialty("Ortodoncia");
        request.setMedicalLicense("MED123");
        request.setPhotoUrl("photo.jpg");

        when(userRepository.existsByEmail("doctor@test.com")).thenReturn(false);
        when(roleRepository.findByNombre("doctor")).thenReturn(Optional.of(buildRole("doctor")));

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1);
            return user;
        });

        UsuarioCreadoResponse response = userService.createUser(request);

        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals("Carlos", response.getName());
        assertEquals("Perez", response.getLastname());
        assertEquals("doctor@test.com", response.getEmail());
        assertEquals("3001234567", response.getPhone());
        assertTrue(response.getActive());
        assertEquals(UserType.DOCTOR, response.getUserType());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_DeberiaCrearPaciente_CuandoDatosSonValidos() {
        CrearUsuarioRequest request = new CrearUsuarioRequest();
        request.setName("Laura");
        request.setLastname("Gomez");
        request.setEmail("patient@test.com");
        request.setPassword("123456");
        request.setPhone("3011234567");
        request.setUserType(UserType.PATIENT);
        request.setBloodType("O+");
        request.setAllergies("Ninguna");
        request.setAddress("Calle 123");

        when(userRepository.existsByEmail("patient@test.com")).thenReturn(false);
        when(roleRepository.findByNombre("patient")).thenReturn(Optional.of(buildRole("patient")));

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(2);
            return user;
        });

        UsuarioCreadoResponse response = userService.createUser(request);

        assertNotNull(response);
        assertEquals(2, response.getId());
        assertEquals("Laura", response.getName());
        assertEquals("patient@test.com", response.getEmail());
        assertEquals(UserType.PATIENT, response.getUserType());
        assertTrue(response.getActive());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_DeberiaLanzarBadRequest_CuandoEmailYaExiste() {
        CrearUsuarioRequest request = new CrearUsuarioRequest();
        request.setEmail("doctor@test.com");
        request.setUserType(UserType.DOCTOR);

        when(userRepository.existsByEmail("doctor@test.com")).thenReturn(true);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> userService.createUser(request)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("El email ya está registrado", exception.getReason());

        verify(userRepository, never()).save(any(User.class));
    }

    private Role buildRole(String name) {
        Role role = new Role();
        role.setId(1);
        role.setNombre(name);
        return role;
    }
}
