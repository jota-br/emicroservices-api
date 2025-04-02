package io.github.jotabrc.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Accessors(chain = true)
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tb_order_status_history")
public class OrderStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 40, nullable = false)
    private String uuid;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    private OrderStatus orderStatus;

    @CreationTimestamp
    @Column(name = "changed_at")
    private LocalDateTime changedAt;

    @Version
    private int version;
}
