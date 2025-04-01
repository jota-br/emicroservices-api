package ostro.veda.user_ms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ostro.veda.user_ms.model.State;

import java.util.Optional;

public interface StateRepository extends JpaRepository<State, Long> {

    Optional<State> findByName(String name);
}
