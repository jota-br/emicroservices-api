package io.github.jotabrc.util.sanitization;

import io.github.jotabrc.document.Category;
import io.github.jotabrc.ov_sanitizer.Sanitizer;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CategorySanitizer implements SanitizerInterface<Category, Category> {

    private final Sanitizer<String, String> sanitizer;

    @Autowired
    public CategorySanitizer(Sanitizer<String, String> sanitizer) {
        this.sanitizer = sanitizer;
    }

    @Override
    public Category sanitize(@NotNull Category category) {

        category
                .setName(sanitizer.sanitize(category.getName()))
                .setDescription(sanitizer.sanitize(category.getDescription()));

        return category;
    }
}
