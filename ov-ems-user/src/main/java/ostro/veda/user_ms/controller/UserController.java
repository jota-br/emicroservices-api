package ostro.veda.user_ms.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ostro.veda.user_ms.dto.UserAddDto;
import ostro.veda.user_ms.dto.UserDto;
import ostro.veda.user_ms.response.ResponsePayload;
import ostro.veda.user_ms.service.UserService;

import java.net.URI;
import java.security.NoSuchAlgorithmException;

import static ostro.veda.user_ms.controller.ControllerDefaults.MAPPING_PREFIX;
import static ostro.veda.user_ms.controller.ControllerDefaults.MAPPING_VERSION_SUFFIX;

@RequestMapping(MAPPING_PREFIX + MAPPING_VERSION_SUFFIX + "/user")
@RestController
@Tag(name = "Order Controller", description = "Manage User operations")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/add")
    public ResponseEntity<ResponsePayload<UserDto>> add(@RequestBody final UserAddDto userAddDto) throws NoSuchAlgorithmException {
        String uuid = userService.add(userAddDto);
        URI location = ServletUriComponentsBuilder
                .fromPath(MAPPING_PREFIX + MAPPING_VERSION_SUFFIX + "/user/uuid/{uuid}")
                .buildAndExpand(uuid)
                .toUri();
        return ResponseEntity.created(location).body(new ResponsePayload<UserDto>()
                .setMessage("User inserted with uuid %s".formatted(uuid)));
    }
}
