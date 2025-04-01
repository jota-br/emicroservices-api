package ostro.veda.user_ms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ostro.veda.user_ms.model.Country;

import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country, Long> {

    Optional<Country> findByName(String name);
}
