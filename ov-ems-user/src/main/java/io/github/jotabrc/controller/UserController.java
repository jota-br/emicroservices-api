package io.github.jotabrc.controller;

import io.github.jotabrc.dto.*;
import io.github.jotabrc.ov_annotation_validator.config.ValidationConfig;
import io.github.jotabrc.response.ResponseBody;
import io.github.jotabrc.response.ResponsePayload;
import io.github.jotabrc.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.security.auth.message.AuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static io.github.jotabrc.controller.ControllerDefaults.MAPPING_PREFIX;
import static io.github.jotabrc.controller.ControllerDefaults.MAPPING_VERSION_SUFFIX;

@RequestMapping(MAPPING_PREFIX + MAPPING_VERSION_SUFFIX + "/user")
@RestController
@Tag(name = "User Controller", description = "Manage User operations")
public class UserController {

    private final UserService userService;
    private final ValidationConfig validationConfig;

    @Autowired
    public UserController(UserService userService, ValidationConfig validationConfig) {
        this.userService = userService;
        this.validationConfig = validationConfig;
    }

    @PostMapping("/register")
    public ResponseEntity<ResponsePayload<UserDto>> add(
            @RequestBody final AddUserDto addUserDto
    ) throws NoSuchAlgorithmException {

        String uuid = userService.add(addUserDto);
        URI location = ServletUriComponentsBuilder
                .fromPath(MAPPING_PREFIX + MAPPING_VERSION_SUFFIX + "/user/{uuid}")
                .buildAndExpand(uuid)
                .toUri();
        return ResponseEntity.created(location).body(new ResponsePayload<UserDto>()
                .setMessage("User inserted with uuid %s".formatted(uuid)));
    }

    @PostMapping("/add/address")
    public ResponseEntity<ResponsePayload<UserDto>> addAddress(
            @RequestBody final AddUserAddressDto addUserAddressDto
    ) throws NoSuchAlgorithmException {

        String uuid = userService.addAddress(addUserAddressDto);
        URI location = ServletUriComponentsBuilder
                .fromPath(MAPPING_PREFIX + MAPPING_VERSION_SUFFIX + "/address/{uuid}")
                .buildAndExpand(uuid)
                .toUri();
        return ResponseEntity.created(location).body(new ResponsePayload<UserDto>()
                .setMessage("User with uuid %s Address inserted".formatted(uuid)));
    }

    @PutMapping("/update")
    public ResponseEntity<ResponsePayload<UserDto>> update(
            @RequestBody final UpdateUserDto updateUserDto
    ) throws NoSuchAlgorithmException {

        userService.update(updateUserDto);
        return ResponseEntity.ok(new ResponsePayload<UserDto>()
                .setMessage("User with uuid %s has been updated".formatted(updateUserDto.getUuid())));
    }

    @PutMapping("/update/password")
    public ResponseEntity<ResponsePayload<UserDto>> updatePassword(
            @RequestBody final UpdateUserPasswordDto updateUserPasswordDto
    ) throws NoSuchAlgorithmException {

        userService.updatePassword(updateUserPasswordDto);
        return ResponseEntity.ok(new ResponsePayload<UserDto>()
                .setMessage("User with uuid %s password updated".formatted(updateUserPasswordDto.getUuid())));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ResponsePayload<UserDto>> getUserByUuid(
            @PathVariable("uuid") final String uuid
    ) throws NoSuchAlgorithmException {

        UserDto userDto = userService.getUserByUuid(uuid);
        return ResponseEntity.ok(new ResponsePayload<UserDto>()
                .setMessage("User with uuid %s found".formatted(uuid))
                .setBody(new ResponseBody<UserDto>()
                        .setData(List.of(userDto))));
    }

    @PostMapping("/login")
    public ResponseEntity<ResponsePayload<UserSessionDto>> login(
            @RequestBody final LoginDto loginDto
    ) throws NoSuchAlgorithmException, AuthException {

        UserSessionDto userSessionDto = userService.login(loginDto);
        return ResponseEntity.ok(new ResponsePayload<UserSessionDto>()
                .setMessage("User with username %s found".formatted(loginDto.getUsername()))
                .setBody(new ResponseBody<UserSessionDto>()
                        .setData(List.of(userSessionDto))));
    }
}
