package ostro.veda.inventory_ms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ostro.veda.inventory_ms.model.Item;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("Select i From tb_item i JOIN tb_location l ON i.productUuid = :productUuid And l.uuid = :locationUuid")
    Optional<Item> findByLocationUuidAndProductUuid(@Param("locationUuid") String locationUuid, @Param("productUuid") String productUuid);

    boolean existsByUuid(String uuid);
}
