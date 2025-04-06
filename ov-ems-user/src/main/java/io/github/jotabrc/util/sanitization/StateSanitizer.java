package io.github.jotabrc.util.sanitization;

import io.github.jotabrc.model.State;
import io.github.jotabrc.ov_sanitizer.Sanitizer;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StateSanitizer implements SanitizerInterface<State, State> {

    private final CountrySanitizer countrySanitizer;
    private final Sanitizer<String, String> sanitizer;

    @Autowired
    public StateSanitizer(CountrySanitizer countrySanitizer, Sanitizer<String, String> sanitizer) {
        this.countrySanitizer = countrySanitizer;
        this.sanitizer = sanitizer;
    }

    @Override
    public State sanitize(@NotNull State state) {
        state
                .setName(sanitizer.sanitize(state.getName()))
                .setCountry(countrySanitizer.sanitize(state.getCountry()));

        return state;
    }
}
