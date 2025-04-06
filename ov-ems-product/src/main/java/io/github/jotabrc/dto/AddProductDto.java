package io.github.jotabrc.dto;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class AddProductDto extends AbsProductDto {

    public AddProductDto(String name, String description, BigDecimal price, boolean isActive, List<CategoryDto> categories, List<ImageDto> images) {
        super(name, description, price, isActive, categories, images);
    }
}
