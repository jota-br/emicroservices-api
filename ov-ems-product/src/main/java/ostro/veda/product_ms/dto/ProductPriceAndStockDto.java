package ostro.veda.product_ms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Accessors(chain = true)
@Getter
@Builder
@AllArgsConstructor
public class ProductPriceAndStockDto {

    private final String uuid;
    private final BigDecimal price;
    private final int stock;
}
