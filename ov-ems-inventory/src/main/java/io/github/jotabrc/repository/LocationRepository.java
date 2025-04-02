package io.github.jotabrc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import io.github.jotabrc.model.Location;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {

    Optional<Location> findByUuid(String uuid);

    boolean existsByName(String name);
}
