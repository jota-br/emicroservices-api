package ostro.veda.product_ms.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ostro.veda.product_ms.document.Product;

public interface ProductRepository extends MongoRepository<Product, String> {
}
