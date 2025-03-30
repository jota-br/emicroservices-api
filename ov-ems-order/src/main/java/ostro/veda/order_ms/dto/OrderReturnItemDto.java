package ostro.veda.order_ms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Accessors(chain = true)
@Getter
@Builder
@AllArgsConstructor
public class OrderReturnItemDto {

    private final String orderUuid;
    private final String productUuid;
    private final String productName;
    private final int quantity;
    private final BigDecimal unitPrice;

}
