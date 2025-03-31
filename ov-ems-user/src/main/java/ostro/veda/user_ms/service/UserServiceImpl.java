package ostro.veda.user_ms.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ostro.veda.user_ms.dto.UserAddDto;
import ostro.veda.user_ms.model.Role;
import ostro.veda.user_ms.model.User;
import ostro.veda.user_ms.repository.RoleRepository;
import ostro.veda.user_ms.repository.UserRepository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public String add(UserAddDto userAddDto) throws NoSuchAlgorithmException {
        boolean exists = userRepository.existsByUsername(userAddDto.getUsername());
        if (exists) throw new EntityExistsException("Username %s already in use".formatted(userAddDto.getUsername()));


        exists = userRepository.existsByEmail(userAddDto.getEmail());
        if (exists) throw new EntityExistsException("Email %s already in use".formatted(userAddDto.getEmail()));

        User user = build(userAddDto);

        return userRepository.save(user).getUuid();
    }

    private User build(UserAddDto userAddDto) throws NoSuchAlgorithmException {
        byte[] salt = getSalt();
        String encodedSalt = getEncodedSalt(salt);
        String hash = getHash(userAddDto.getPassword(), salt);

        Role role = roleRepository.findByName("GUESTS")
                .orElseThrow(() -> new EntityNotFoundException("Role with name GUESTS not found"));

        return User
                .builder()
                .uuid(UUID.randomUUID().toString())
                .username(userAddDto.getUsername())
                .email(userAddDto.getEmail())
                .salt(encodedSalt)
                .hash(hash)
                .firstName(userAddDto.getFirstName())
                .lastName(userAddDto.getLastName())
                .phone(userAddDto.getPhone())
                .role(role)
                .build();
    }

    private String getEncodedSalt(byte[] salt) throws NoSuchAlgorithmException {
        return Base64.getEncoder().encodeToString(getSalt());
    }

    private byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[32];
        sr.nextBytes(salt);
        return salt;
    }

    private String getHash(String password, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(salt);
        byte[] hashedPassword = md.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hashedPassword);
    }

    public RoleRepository getRoleRepository() {
        return roleRepository;
    }
}
