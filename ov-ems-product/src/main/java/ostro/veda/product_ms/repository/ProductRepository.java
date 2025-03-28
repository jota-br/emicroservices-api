package ostro.veda.product_ms.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import ostro.veda.product_ms.document.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product, String> {

    Optional<Product> findByName(String name);

    @Query("{ 'categories.name': ?0}")
    List<Product> findByCategoryName(String category);

    Optional<Product> findByUuid(String uuid);
}
