package ostro.veda.order_ms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ostro.veda.order_ms.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
