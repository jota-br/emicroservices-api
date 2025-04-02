package io.github.jotabrc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import io.github.jotabrc.model.Country;

import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country, Long> {

    Optional<Country> findByName(String name);
}
