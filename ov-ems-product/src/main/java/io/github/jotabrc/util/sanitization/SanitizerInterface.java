package io.github.jotabrc.util.sanitization;

import jakarta.validation.constraints.NotNull;

public interface SanitizerInterface<T, R> {

    R sanitize(@NotNull T t);
}
