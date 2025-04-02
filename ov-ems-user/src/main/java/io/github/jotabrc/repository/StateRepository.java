package io.github.jotabrc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import io.github.jotabrc.model.State;

import java.util.Optional;

public interface StateRepository extends JpaRepository<State, Long> {

    Optional<State> findByName(String name);
}
