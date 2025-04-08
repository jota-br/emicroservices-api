package io.github.jotabrc.controller;

import io.github.jotabrc.dto.UserDto;
import io.github.jotabrc.response.ResponsePayload;
import io.github.jotabrc.service.ActivationTokenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static io.github.jotabrc.controller.ControllerDefaults.MAPPING_PREFIX;
import static io.github.jotabrc.controller.ControllerDefaults.MAPPING_VERSION_SUFFIX;

@RequestMapping(MAPPING_PREFIX + MAPPING_VERSION_SUFFIX + "/activation-token")
@RestController
@Tag(name = "Activation Token Controller", description = "Manage Activation Token operations")
public class ActivationTokenController {

    private final ActivationTokenService activationTokenService;

    @Autowired
    public ActivationTokenController(ActivationTokenService activationTokenService) {
        this.activationTokenService = activationTokenService;
    }

    @GetMapping("/add/{uuid}")
    public ResponseEntity<ResponsePayload<UserDto>> add(@PathVariable("uuid") final String userUuid) {
        activationTokenService.add(userUuid);
        return ResponseEntity.ok(
                new ResponsePayload<UserDto>()
                        .setMessage("Activation Token created")
        );
    }

    @PutMapping("/activate/{token}")
    public ResponseEntity<ResponsePayload<UserDto>> activate(
            @PathVariable("token") final String token
    ) {
        String uuid = activationTokenService.activate(token);
        return ResponseEntity.ok(
                new ResponsePayload<UserDto>()
                        .setMessage("User with uuid %s activated".formatted(uuid))
        );
    }
}
