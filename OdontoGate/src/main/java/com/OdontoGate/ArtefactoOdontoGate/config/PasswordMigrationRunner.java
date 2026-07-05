package com.OdontoGate.ArtefactoOdontoGate.config;

import com.OdontoGate.ArtefactoOdontoGate.model.User;
import com.OdontoGate.ArtefactoOdontoGate.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PasswordMigrationRunner implements ApplicationRunner {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(PasswordMigrationRunner.class);
    private static final String BCRYPT_PATTERN =
            "^\\$2[aby]\\$\\d{2}\\$[./A-Za-z0-9]{53}$";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public PasswordMigrationRunner(UserRepository userRepository,
                                   PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        int migratedPasswords = 0;

        for (User user : userRepository.findAll()) {
            String currentPassword = user.getPassword();

            if (currentPassword == null || isBcryptHash(currentPassword)) {
                continue;
            }

            user.setPassword(passwordEncoder.encode(currentPassword));
            migratedPasswords++;
        }

        if (migratedPasswords > 0) {
            LOGGER.info("Migrated {} plaintext user passwords to BCrypt.",
                    migratedPasswords);
        }
    }

    private boolean isBcryptHash(String password) {
        return password.matches(BCRYPT_PATTERN);
    }
}
