package io.github.jotabrc.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Accessors(chain = true)
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tb_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 40, nullable = false)
    private String uuid;

    @Column(name = "user_uuid", length = 40, nullable = false)
    private String userUuid;

    @Column(name = "user_email", length = 320, nullable = false)
    private String userEmail;

    @Column(name = "shipping_address", length = 500, nullable = false)
    private String shippingAddress;

    @Column(name = "billing_address", length = 500, nullable = false)
    private String billingAddress;

    @CreationTimestamp
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private OrderStatus status;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderStatusHistory> orderStatusHistories;

    @Version
    private int version;

}
