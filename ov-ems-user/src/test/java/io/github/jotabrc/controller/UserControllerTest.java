package io.github.jotabrc.controller;

import io.github.jotabrc.dto.UserDto;
import io.github.jotabrc.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ActiveProfiles("dev")
class UserControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .build();
    }

    @Test
    void add() throws Exception {
    }

    @Test
    void addAddress() {
    }

    @Test
    void update() {
    }

    @Test
    void updatePassword() {
    }

    @Test
    void getUserByUuid() throws Exception {
        UserDto userDto = UserDto
                .builder()
                .uuid(UUID.randomUUID().toString())
                .username("username32")
                .email("user@email.com")
                .firstName("First")
                .lastName("Last")
                .phone("+5511123456789")
                .build();
        when(userService.getUserByUuid(userDto.getUuid())).thenReturn(userDto);

        mockMvc.perform(get("/api/v1/user/get/uuid/" + userDto.getUuid()))
                .andExpect(status().isOk());
    }

    @Test
    void login() {
    }
}