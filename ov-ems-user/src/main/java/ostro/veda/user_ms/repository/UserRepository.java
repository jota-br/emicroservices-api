package ostro.veda.user_ms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ostro.veda.user_ms.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUuid(String uuid);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String username);

    boolean existsByPhone(String phone);
}
