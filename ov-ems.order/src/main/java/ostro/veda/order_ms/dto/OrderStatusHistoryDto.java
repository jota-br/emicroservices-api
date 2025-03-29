package ostro.veda.order_ms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;
import ostro.veda.order_ms.model.OrderStatus;

import java.time.LocalDateTime;

@Accessors(chain = true)
@Getter
@Builder
@AllArgsConstructor
public class OrderStatusHistoryDto {

    private final String uuid;
    private final long orderId;
    private final OrderStatus orderStatus;
    private final LocalDateTime changedAt;
}
