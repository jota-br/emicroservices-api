package ostro.veda.product_ms.service;

import ostro.veda.product_ms.document.Product;
import ostro.veda.product_ms.dto.ProductDto;

public interface ProductService {

    ProductDto get(String name);

    void add(ProductDto productDto);

    Product build(ProductDto productDto);

    ProductDto toDto(Product product);
}
