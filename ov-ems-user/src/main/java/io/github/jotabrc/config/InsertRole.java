package io.github.jotabrc.config;

import io.github.jotabrc.util.UserRoles;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import io.github.jotabrc.model.Role;
import io.github.jotabrc.repository.RoleRepository;

import java.util.List;
import java.util.UUID;

@Component
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
                        .build()
        );

        roles.forEach(roleRepository::save);
    }
}
