package io.github.jotabrc.service;

import io.github.jotabrc.dto.ProductDto;
import io.github.jotabrc.dto.ProductPriceDto;

import java.util.List;

public interface ProductService {

    ProductDto getByName(String name);

    List<ProductDto> getByCategories(String category);

    ProductDto getByUuid(String uuid);

    String add(ProductDto productDto);

    void update(ProductDto productDto);

    void updatePrice(ProductPriceDto productPriceDto);
}
