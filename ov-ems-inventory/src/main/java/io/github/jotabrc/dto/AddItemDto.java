package io.github.jotabrc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Builder
@AllArgsConstructor
public class AddItemDto {

    private final String uuid;
    private final String productUuid;
    private final String productName;
    private final int stock;
    private final int reserved;
    private final String locationUuid;
}
