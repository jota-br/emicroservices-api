package ostro.veda.product_ms.service;

import ostro.veda.product_ms.document.Product;
import ostro.veda.product_ms.dto.ProductDto;

import java.util.List;

public interface ProductService {

    ProductDto getByName(String name);

    List<ProductDto> getByCategories(String category);

    ProductDto getByUuid(String uuid);

    String add(ProductDto productDto);

    String update(ProductDto productDto);

    Product build(ProductDto productDto);

    List<ProductDto> toDto(List<Product> products);

    ProductDto toDto(Product product);
}
