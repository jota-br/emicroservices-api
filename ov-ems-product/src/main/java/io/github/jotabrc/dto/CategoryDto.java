package io.github.jotabrc.dto;

import lombok.*;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Builder
@AllArgsConstructor
public class CategoryDto {

    private final String name;
    private final String description;
    private final boolean isActive;
}
