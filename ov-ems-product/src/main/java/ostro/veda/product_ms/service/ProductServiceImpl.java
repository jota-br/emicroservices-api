package ostro.veda.product_ms.service;

import org.springframework.stereotype.Service;
import ostro.veda.product_ms.document.Category;
import ostro.veda.product_ms.document.Image;
import ostro.veda.product_ms.document.Product;
import ostro.veda.product_ms.dto.CategoryDto;
import ostro.veda.product_ms.dto.ImageDto;
import ostro.veda.product_ms.dto.ProductDto;
import ostro.veda.product_ms.exception.DocumentAlreadyExists;
import ostro.veda.product_ms.exception.DocumentNotFound;
import ostro.veda.product_ms.repository.ProductRepository;

import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductDto get(String name) {
        Product product = productRepository.findByName(name)
                .orElseThrow(() -> new DocumentNotFound("Product with name %s not found".formatted(name)));
        return toDto(product);
    }

    @Override
    public void add(ProductDto productDto) {
        boolean productExists = productRepository.findByName(productDto.getName())
                .isPresent();

        if (productExists) throw new DocumentAlreadyExists("Product with name %s already exists"
                .formatted(productDto.getName()));

        Product product = build(productDto);
        productRepository.save(product);
    }

    @Override
    public Product build(ProductDto productDto) {
        return new Product()
                .setUuid(UUID.randomUUID().toString())
                .setName(productDto.getName())
                .setDescription(productDto.getDescription())
                .setPrice(productDto.getPrice())
                .setStock(productDto.getStock())
                .setActive(productDto.isActive())
                .setCategories(productDto.getCategories().stream()
                        .map(categoryDto -> new Category()
                                .setName(categoryDto.getName())
                                .setDescription(categoryDto.getDescription())
                                .setActive(categoryDto.isActive()))
                        .toList())
                .setImages(productDto.getImages().stream()
                        .map(imageDto -> new Image()
                                .setImagePath(imageDto.getImagePath())
                                .setMain(imageDto.isMain()))
                        .toList());

    }

    @Override
    public ProductDto toDto(Product product) {
        return new ProductDto()
                .setUuid(UUID.randomUUID().toString())
                .setName(product.getName())
                .setDescription(product.getDescription())
                .setPrice(product.getPrice())
                .setStock(product.getStock())
                .setActive(product.isActive())
                .setCategories(product.getCategories().stream()
                        .map(categoryDto -> new CategoryDto()
                                .setName(categoryDto.getName())
                                .setDescription(categoryDto.getDescription())
                                .setActive(categoryDto.isActive()))
                        .toList())
                .setImages(product.getImages().stream()
                        .map(imageDto -> new ImageDto()
                                .setImagePath(imageDto.getImagePath())
                                .setMain(imageDto.isMain()))
                        .toList());
    }
}
