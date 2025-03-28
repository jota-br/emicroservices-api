package ostro.veda.product_ms.document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Document(collection = "product")
public class Product {

    @Id
    private String id;
    private String uuid;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private boolean isActive;

    @Version
    private int version;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private List<Category> categories = new ArrayList<>();
    private List<Image> images = new ArrayList<>();
}
