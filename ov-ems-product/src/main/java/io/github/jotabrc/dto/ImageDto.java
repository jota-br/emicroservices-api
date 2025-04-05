package io.github.jotabrc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Builder
@AllArgsConstructor
public class ImageDto {

    private final String imagePath;
    private final boolean isMain;
}
