package io.github.jotabrc.model;

import io.github.jotabrc.ov_annotation_validator.annotation.ValidateExpiration;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
    @Column(nullable = false)
    private LocalDateTime expiration;


    @Column(length = 40, nullable = false, unique = true)
    private String token;

    @Column(name = "user_uuid", length = 40, nullable = false)
    private String userUuid;

    private boolean used;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Version
    private int version;
}
