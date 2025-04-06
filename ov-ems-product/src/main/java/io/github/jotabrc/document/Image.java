package io.github.jotabrc.document;

import lombok.*;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Image {

    private String imagePath;
    private boolean isMain;
}
