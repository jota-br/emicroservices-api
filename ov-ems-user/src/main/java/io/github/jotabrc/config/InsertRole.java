package io.github.jotabrc.config;

import io.github.jotabrc.model.Role;
import io.github.jotabrc.ov_auth_validator.util.UserRoles;
import io.github.jotabrc.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@Order(1)
public class InsertRole implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public InsertRole(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        List<Role> roles = List.of(
                Role
                        .builder()
                        .uuid(UUID.randomUUID().toString())
                        .name(UserRoles.GUEST.getName())
                        .description("Guest - Inactive User")
                        .isActive(true)
                        .build(),
                Role
                        .builder()
                        .uuid(UUID.randomUUID().toString())
                        .name(UserRoles.USER.getName())
                        .description("Active User")
                        .isActive(true)
                        .build(),
                Role
                        .builder()
                        .uuid(UUID.randomUUID().toString())
                        .name(UserRoles.ADMIN.getName())
                        .description("Admin")
                        .isActive(true)
                        .build(),
                Role
                        .builder()
                        .uuid(UUID.randomUUID().toString())
                        .name(UserRoles.SYSTEM.getName())
                        .description("System")
                        .isActive(true)
                        .build()
        );

        roles.forEach(roleRepository::save);
    }
}
