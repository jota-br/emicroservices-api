package ostro.veda.order_ms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;
import ostro.veda.order_ms.model.OrderStatus;

@Accessors(chain = true)
@Getter
@Builder
@AllArgsConstructor
public class OrderStatusUpdateDto {

    private final String uuid;
    private final OrderStatus status;
}
