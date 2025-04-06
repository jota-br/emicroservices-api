package io.github.jotabrc.util.sanitization;

import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

@Component
public interface SanitizerInterface<T, R> {

    R sanitize(@NotNull T t);
}
