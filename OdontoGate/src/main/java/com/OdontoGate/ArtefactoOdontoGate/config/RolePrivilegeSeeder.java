package com.OdontoGate.ArtefactoOdontoGate.config;

import com.OdontoGate.ArtefactoOdontoGate.model.Privilege;
import com.OdontoGate.ArtefactoOdontoGate.model.Role;
import com.OdontoGate.ArtefactoOdontoGate.model.User;
import com.OdontoGate.ArtefactoOdontoGate.repository.AdministratorRepository;
import com.OdontoGate.ArtefactoOdontoGate.repository.DoctorRepository;
import com.OdontoGate.ArtefactoOdontoGate.repository.PatientRepository;
import com.OdontoGate.ArtefactoOdontoGate.repository.RoleRepository;
import com.OdontoGate.ArtefactoOdontoGate.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RolePrivilegeSeeder implements CommandLineRunner {

    private static final String ADMINISTRATOR = "administrator";
    private static final String DOCTOR = "doctor";
    private static final String PATIENT = "patient";

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final AdministratorRepository administratorRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public RolePrivilegeSeeder(
            RoleRepository roleRepository,
            UserRepository userRepository,
            AdministratorRepository administratorRepository,
            DoctorRepository doctorRepository,
            PatientRepository patientRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.administratorRepository = administratorRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        createRoleIfAbsent(ADMINISTRATOR, List.of("ADMINISTRATOR_ACCESO_TOTAL"));
        createRoleIfAbsent(DOCTOR, List.of(
                "DOCTOR_VER_PACIENTES_AGENDADOS",
                "DOCTOR_VER_CITAS_AGENDADAS",
                "DOCTOR_MODIFICAR_HISTORIA_CLINICA",
                "DOCTOR_CREAR_HISTORIA_CLINICA",
                "DOCTOR_LEER_HISTORIA_CLINICA"));
        createRoleIfAbsent(PATIENT, List.of(
                "PATIENT_CREAR_CITA",
                "PATIENT_LEER_CITA",
                "PATIENT_MODIFICAR_CITA",
                "PATIENT_ELIMINAR_CITA"));
        assignRolesToExistingUsers();
    }

    private void createRoleIfAbsent(String roleName, List<String> privilegeNames) {
        if (roleRepository.existsByNombre(roleName)) {
            return;
        }

        Role role = new Role();
        role.setNombre(roleName);

        List<Privilege> privileges = privilegeNames.stream()
                .map(privilegeName -> buildPrivilege(role, privilegeName))
                .toList();

        role.getPrivileges().addAll(privileges);
        roleRepository.save(role);
    }

    private Privilege buildPrivilege(Role role, String privilegeName) {
        Privilege privilege = new Privilege();
        privilege.setRole(role);
        privilege.setNombre(privilegeName);
        return privilege;
    }

    private void assignRolesToExistingUsers() {
        userRepository.findAll()
                .stream()
                .filter(user -> user.getRole() == null)
                .forEach(this::assignRoleToExistingUser);
    }

    private void assignRoleToExistingUser(User user) {
        Optional<Role> role = findRoleForExistingUser(user.getId());

        if (role.isEmpty()) {
            return;
        }

        user.setRole(role.get());
        userRepository.save(user);
    }

    private Optional<Role> findRoleForExistingUser(Integer userId) {
        if (administratorRepository.existsById(userId)) {
            return roleRepository.findByNombre(ADMINISTRATOR);
        }

        if (doctorRepository.existsById(userId)) {
            return roleRepository.findByNombre(DOCTOR);
        }

        if (patientRepository.existsById(userId)) {
            return roleRepository.findByNombre(PATIENT);
        }

        return Optional.empty();
    }
}
