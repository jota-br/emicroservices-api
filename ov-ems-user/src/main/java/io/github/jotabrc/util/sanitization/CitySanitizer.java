package io.github.jotabrc.util.sanitization;

import io.github.jotabrc.model.City;
import io.github.jotabrc.ov_sanitizer.Sanitizer;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CitySanitizer implements SanitizerInterface<City, City> {

    private final StateSanitizer stateSanitizer;
    private final Sanitizer<String, String> sanitizer;

    @Autowired
    public CitySanitizer(StateSanitizer stateSanitizer, Sanitizer<String, String> sanitizer) {
        this.stateSanitizer = stateSanitizer;
        this.sanitizer = sanitizer;
    }

    @Override
    public City sanitize(@NotNull City city) {
        city
                .setName(sanitizer.sanitize(city.getName()))
                .setState(stateSanitizer.sanitize(city.getState()));

        return city;
    }
}
