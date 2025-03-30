package ostro.veda.inventory_ms.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Accessors(chain = true)
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tb_item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 40)
    private String uuid;

    @Column(name = "product_uuid", nullable = false)
    private String productUuid;

    @Column(name = "product_name", nullable = false)
    private String productName;
    private int stock;
    private int reserved;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Version
    private int version;
}
