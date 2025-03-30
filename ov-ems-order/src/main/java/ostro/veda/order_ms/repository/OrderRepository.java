package ostro.veda.order_ms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ostro.veda.order_ms.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByUuid(String uuid);

    List<Order> findByUserUuid(String uuid);
}
