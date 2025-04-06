package io.github.jotabrc.util.sanitization;

import io.github.jotabrc.model.Country;
import io.github.jotabrc.ov_sanitizer.Sanitizer;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CountrySanitizer implements SanitizerInterface<Country, Country> {

    private final Sanitizer<String, String> sanitizer;

    @Autowired
    public CountrySanitizer(Sanitizer<String, String> sanitizer) {
        this.sanitizer = sanitizer;
    }

    @Override
    public Country sanitize(@NotNull Country country) {

        country
                .setName(sanitizer.sanitize(country.getName()));

        return country;
    }
}
