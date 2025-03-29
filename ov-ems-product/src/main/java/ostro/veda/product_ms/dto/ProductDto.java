package ostro.veda.product_ms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Accessors(chain = true)
@Getter
@Builder
@AllArgsConstructor
public class ProductDto {

    private final String uuid;
    private final String name;
    private final String description;
    private final BigDecimal price;
    private final boolean isActive;
    private final List<CategoryDto> categories;
    private final List<ImageDto> images;
}
