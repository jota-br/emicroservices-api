package ostro.veda.user_ms.dto;

import lombok.*;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserExistsFieldsDto {

    private String username;
    private String email;
    private String phone;
}
