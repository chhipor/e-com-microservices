package su35.setec.authservice.config;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import su35.setec.authservice.entity.Permission;
import su35.setec.authservice.entity.Role;
import su35.setec.authservice.entity.User;
import su35.setec.authservice.repository.PermissionRepository;
import su35.setec.authservice.repository.RoleRepository;
import su35.setec.authservice.repository.UserRepository;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    @Bean
    public CommandLineRunner seedDefaultAdmin(
            PermissionRepository permissionRepository,
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AdminProperties adminProperties,
            PlatformTransactionManager transactionManager
    ) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        return args -> transactionTemplate.executeWithoutResult(status ->
                seed(permissionRepository, roleRepository, userRepository, passwordEncoder, adminProperties));
    }

    void seed(
            PermissionRepository permissionRepository,
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AdminProperties adminProperties
    ) {
        Permission userRead = permissionRepository.findByName("USER_READ")
                .orElseGet(() -> permissionRepository.save(permission("USER_READ", "Read user records")));
        Permission userWrite = permissionRepository.findByName("USER_WRITE")
                .orElseGet(() -> permissionRepository.save(permission("USER_WRITE", "Create user records")));
        Permission userUpdate = permissionRepository.findByName("USER_UPDATE")
                .orElseGet(() -> permissionRepository.save(permission("USER_UPDATE", "Update user records")));
        Permission userDelete = permissionRepository.findByName("USER_DELETE")
                .orElseGet(() -> permissionRepository.save(permission("USER_DELETE", "Delete user records")));

        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseGet(() -> roleRepository.save(role("ADMIN", "Administrator role")));
        adminRole.getPermissions().addAll(Set.of(userRead, userWrite, userUpdate, userDelete));
        roleRepository.save(adminRole);

        roleRepository.findByName("USER")
                .orElseGet(() -> roleRepository.save(role("USER", "Standard user role")));

        if (userRepository.findByEmail(adminProperties.email()).isEmpty()) {
            User admin = User.builder()
                    .firstName(adminProperties.firstName())
                    .lastName(adminProperties.lastName())
                    .email(adminProperties.email())
                    .password(passwordEncoder.encode(adminProperties.password()))
                    .enabled(true)
                    .accountNonLocked(true)
                    .roles(Set.of(adminRole))
                    .build();
            userRepository.save(admin);
        }
    }

    private Permission permission(String name, String description) {
        return Permission.builder().name(name).description(description).build();
    }

    private Role role(String name, String description) {
        return Role.builder().name(name).description(description).build();
    }
}
