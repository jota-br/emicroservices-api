package io.github.jotabrc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import io.github.jotabrc.model.Address;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

    Optional<Address> findByUuid(String uuid);
}
