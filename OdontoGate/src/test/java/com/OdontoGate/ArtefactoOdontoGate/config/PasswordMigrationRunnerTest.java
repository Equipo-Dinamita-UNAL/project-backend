package com.OdontoGate.ArtefactoOdontoGate.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.OdontoGate.ArtefactoOdontoGate.model.User;
import com.OdontoGate.ArtefactoOdontoGate.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class PasswordMigrationRunnerTest {

    private static final String EXISTING_BCRYPT_HASH =
            "$2a$10$123456789012345678901u1234567890123456789012345678901";

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void run_DeberiaCifrarSoloContrasenasEnTextoPlano() {
        User legacyUser = new User();
        legacyUser.setPassword("123456");

        User migratedUser = new User();
        migratedUser.setPassword(EXISTING_BCRYPT_HASH);

        User userWithoutPassword = new User();

        when(userRepository.findAll())
                .thenReturn(List.of(legacyUser, migratedUser, userWithoutPassword));
        when(passwordEncoder.encode("123456")).thenReturn("encoded-123456");

        PasswordMigrationRunner runner =
                new PasswordMigrationRunner(userRepository, passwordEncoder);

        runner.run(null);

        assertEquals("encoded-123456", legacyUser.getPassword());
        assertEquals(EXISTING_BCRYPT_HASH, migratedUser.getPassword());
        verify(passwordEncoder).encode("123456");
        verify(passwordEncoder, never()).encode(EXISTING_BCRYPT_HASH);
    }
}
