package ostro.veda.inventory_ms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ostro.veda.inventory_ms.model.Location;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {

    Optional<Location> findByUuid(String uuid);

    boolean existsByName(String name);
}
