package io.github.jotabrc.dto;

import lombok.*;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Builder
@AllArgsConstructor
public class CityDto {

    private final String uuid;
    private final String name;
    private final StateDto state;
}
