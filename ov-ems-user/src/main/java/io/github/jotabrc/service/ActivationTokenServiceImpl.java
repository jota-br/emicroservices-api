package io.github.jotabrc.service;

import io.github.jotabrc.model.ActivationToken;
import io.github.jotabrc.model.Role;
import io.github.jotabrc.model.User;
import io.github.jotabrc.ov_auth_validator.util.UserRoles;
import io.github.jotabrc.repository.ActivationTokenRepository;
import io.github.jotabrc.repository.RoleRepository;
import io.github.jotabrc.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.UUID;

@Validated
@Service
public class ActivationTokenServiceImpl implements ActivationTokenService {

    private final ActivationTokenRepository activationTokenRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public ActivationTokenServiceImpl(ActivationTokenRepository activationTokenRepository, UserRepository userRepository, RoleRepository roleRepository) {
        this.activationTokenRepository = activationTokenRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void add(final String userUuid) {
        ActivationToken activationToken = build(userUuid);
        activationTokenRepository.save(activationToken);
    }

    @Override
    public String activate(final String token) {
        ActivationToken activationToken = activationTokenRepository.findByToken(token)
                .orElseThrow(() -> new EntityNotFoundException("Token %s not found".formatted(token)));

        isUsed(activationToken);

        User user = userRepository.findByUuid(activationToken.getUserUuid())
                .orElseThrow(() -> new EntityNotFoundException("User with uuid %s not found".formatted(activationToken.getUserUuid())));
        Role role = roleRepository.findByName(UserRoles.USER.getName())
                .orElseThrow(() -> new EntityNotFoundException("Role with name %s not found".formatted(UserRoles.USER.getName())));

        activateUser(user, role, activationToken);
        return userRepository.save(user).getUuid();
    }

    private static void isUsed(ActivationToken activationToken) {
        if (activationToken.isUsed()) throw new IllegalStateException("Token %s already used".formatted(activationToken.getToken()));
    }

    private ActivationToken build(final String uuid) {
        final int HOURS_TO_EXPIRE = 1;
        return ActivationToken
                .builder()
                .token(UUID.randomUUID().toString())
                .expiration(LocalDateTime.now().plusHours(HOURS_TO_EXPIRE))
                .userUuid(uuid)
                .used(false)
                .build();
    }

    private void activateUser(final User user, final Role role, @Valid final ActivationToken activationToken) {
        user
                .setActive(true)
                .setRole(role);
    }
}
