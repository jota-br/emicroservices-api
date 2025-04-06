package io.github.jotabrc.document;

import lombok.*;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    private String name;
    private String description;
    private boolean isActive;
}
