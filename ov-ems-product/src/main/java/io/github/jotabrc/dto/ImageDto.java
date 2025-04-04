package io.github.jotabrc.dto;

import io.github.jotabrc.ov_annotation_validator.annotation.ValidateField;
import lombok.*;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Builder
@AllArgsConstructor
public class ImageDto {

    @ValidateField(fieldName = "image-path", message = "Invalid Image Path")
    private final String imagePath;
    private final boolean isMain;
}
