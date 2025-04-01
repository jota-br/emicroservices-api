package ostro.veda.user_ms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ostro.veda.user_ms.model.Address;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

    Optional<Address> findByUuid(String uuid);
}
