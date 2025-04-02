package io.github.jotabrc.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Accessors(chain = true)
@Getter
@Builder
@AllArgsConstructor
@Document(collection = "product")
public class Product {

    @Id
    private final String id;
    private final String uuid;
    private final String name;
    private final String description;
    private final BigDecimal price;
    private final boolean isActive;

    @Version
    private final int version;

    @CreatedDate
    private final LocalDateTime createdAt;

    @LastModifiedDate
    private final LocalDateTime updatedAt;

    private final List<Category> categories;
    private final List<Image> images;
}
