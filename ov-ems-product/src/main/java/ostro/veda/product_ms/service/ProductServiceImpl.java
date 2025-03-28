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

import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductDto getByName(final String name) {
        Product product = productRepository.findByName(name)
                .orElseThrow(() -> new DocumentNotFound("Product with name %s not found".formatted(name)));
        return toDto(product);
    }

    @Override
    public List<ProductDto> getByCategories(final String category) {
        List<Product> product = productRepository.findByCategoryName(category);

        if (product.isEmpty()) throw new DocumentNotFound("Product with category %s not found".formatted(category));
        return toDto(product);
    }

    @Override
    public ProductDto getByUuid(final String uuid) {
        Product product = productRepository.findByUuid(uuid)
                .orElseThrow(() -> new DocumentNotFound("Product with uuid %s not found".formatted(uuid)));
        return toDto(product);
    }

    @Override
    public String add(ProductDto productDto) {
        boolean productExists = productRepository.findByName(productDto.getName())
                .isPresent();

        if (productExists) throw new DocumentAlreadyExists("Product with name %s already exists"
                .formatted(productDto.getName()));

        Product product = build(productDto);
        return productRepository.save(product).getUuid();
    }

    @Override
    public String update(final ProductDto productDto) {
        return "";
    }

    @Override
    public Product build(final ProductDto productDto) {
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
    public List<ProductDto> toDto(final List<Product> products) {
        return products.stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public ProductDto toDto(final Product product) {
        return ProductDto
                .builder()
                .uuid(product.getUuid())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .isActive(product.isActive())
                .categories(product.getCategories().stream()
                        .map(categoryDto -> CategoryDto
                                .builder()
                                .name(categoryDto.getName())
                                .description(categoryDto.getDescription())
                                .isActive(categoryDto.isActive())
                                .build())
                        .toList())
                .images(product.getImages().stream()
                        .map(imageDto -> ImageDto
                                .builder()
                                .imagePath(imageDto.getImagePath())
                                .isMain(imageDto.isMain())
                                .build())
                        .toList())
                .build();
    }
}
