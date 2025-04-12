package io.github.jotabrc.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.jotabrc.ov_annotation_validator.annotation.ValidateField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddItemDto {

    @ValidateField(fieldName = "uuid", message = "Invalid UUID")
    private String uuid;
    private String name;
}
