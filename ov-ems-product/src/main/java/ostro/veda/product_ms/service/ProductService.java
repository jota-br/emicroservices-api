package ostro.veda.product_ms.service;

import ostro.veda.product_ms.dto.ProductDto;
import ostro.veda.product_ms.dto.ProductPriceDto;

import java.util.List;

public interface ProductService {

    ProductDto getByName(String name);

    List<ProductDto> getByCategories(String category);

    ProductDto getByUuid(String uuid);

    String add(ProductDto productDto);

    void update(ProductDto productDto);

    void updatePrice(ProductPriceDto productPriceDto);
}
