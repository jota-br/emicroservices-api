package io.github.jotabrc.util.sanitization;

import io.github.jotabrc.document.Product;
import io.github.jotabrc.ov_sanitizer.Sanitizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductSanitizer implements SanitizerInterface<Product, Product> {

    private final CategorySanitizer categorySanitizer;
    private final Sanitizer<String, String> sanitizer;

    @Autowired
    public ProductSanitizer(CategorySanitizer categorySanitizer, Sanitizer<String, String> sanitizer) {
        this.categorySanitizer = categorySanitizer;
        this.sanitizer = sanitizer;
    }

    @Override
    public Product sanitize(Product product) {

        product
                .setName(sanitizer.sanitize(product.getName()))
                .setDescription(sanitizer.sanitize(product.getDescription()))
                .setCategories(
                        product.getCategories()
                                .stream()
                                .map(categorySanitizer::sanitize)
                                .toList()
                );

        return product;
    }
}
