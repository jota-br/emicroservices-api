package ostro.veda.user_ms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ostro.veda.user_ms.model.City;

import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Long> {

    Optional<City> findByName(String name);
}
