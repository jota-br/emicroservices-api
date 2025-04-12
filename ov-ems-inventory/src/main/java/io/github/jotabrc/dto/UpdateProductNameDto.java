package io.github.jotabrc.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.jotabrc.ov_annotation_validator.annotation.ValidateField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Builder
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateProductNameDto {

    @ValidateField(fieldName = "uuid", message = "Invalid UUID")
    private final String uuid;

    @ValidateField(fieldName = "name", message = "Invalid Name")
    private final String name;
}
