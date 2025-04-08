package io.github.jotabrc.model;

import io.github.jotabrc.ov_annotation_validator.annotation.ValidateExpiration;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Accessors(chain = true)
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tb_user_activation_token")
public class ActivationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ValidateExpiration(fieldName = "expiration", expirationHours = 1, message = "Token expired")
    private LocalDateTime expiration;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private String userUuid;

    private boolean used;
}
