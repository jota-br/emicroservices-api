package ostro.veda.product_ms.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ostro.veda.product_ms.document.Product;

import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product, String> {

    Optional<Product> findByName(String name);
}
