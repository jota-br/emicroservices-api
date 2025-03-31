package ostro.veda.user_ms.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ostro.veda.user_ms.model.Role;
import ostro.veda.user_ms.repository.RoleRepository;

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
                        .name("GUESTS")
                        .description("Guest - Inactive User")
                        .isActive(true)
                        .build(),
                Role
                        .builder()
                        .uuid(UUID.randomUUID().toString())
                        .name("USERS")
                        .description("Active User")
                        .isActive(true)
                        .build(),
                Role
                        .builder()
                        .uuid(UUID.randomUUID().toString())
                        .name("ADMINS")
                        .description("Admin")
                        .isActive(true)
                        .build()
        );

        roles.forEach(roleRepository::save);
    }
}
