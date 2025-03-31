package ostro.veda.user_ms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Builder
@AllArgsConstructor
public class UserAddDto {

    private final String uuid;
    private final String username;
    private final String email;
    private final String password;
    private final String firstName;
    private final String lastName;
    private final String phone;
}
