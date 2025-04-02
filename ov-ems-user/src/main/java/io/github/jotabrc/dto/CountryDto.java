package io.github.jotabrc.dto;

import lombok.*;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Builder
@AllArgsConstructor
public class CountryDto {

    private final String uuid;
    private final String name;
}
