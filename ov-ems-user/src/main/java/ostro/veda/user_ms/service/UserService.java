package ostro.veda.user_ms.service;

import ostro.veda.user_ms.dto.UserAddDto;

import java.security.NoSuchAlgorithmException;

public interface UserService {

    String add(UserAddDto userAddDto) throws NoSuchAlgorithmException;
}
