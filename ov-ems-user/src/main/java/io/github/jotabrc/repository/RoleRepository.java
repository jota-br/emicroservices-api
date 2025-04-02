package io.github.jotabrc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import io.github.jotabrc.model.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);
}
