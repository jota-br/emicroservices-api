package ostro.veda.product_ms.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Accessors(chain = true)
@Setter
@Getter
@NoArgsConstructor
public class ProductDto {

    private String uuid;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private boolean isActive;
    private List<CategoryDto> categories = new ArrayList<>();
    private List<ImageDto> images = new ArrayList<>();
}
