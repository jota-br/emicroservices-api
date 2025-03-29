package ostro.veda.product_ms.service;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import ostro.veda.product_ms.document.Category;
import ostro.veda.product_ms.document.Image;
import ostro.veda.product_ms.document.Product;
import ostro.veda.product_ms.dto.*;
import ostro.veda.product_ms.handler.DocumentAlreadyExistsException;
import ostro.veda.product_ms.handler.DocumentNotFoundException;
import ostro.veda.product_ms.handler.InvalidDataException;
import ostro.veda.product_ms.repository.ProductRepository;

import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final MongoTemplate mongoTemplate;

    public ProductServiceImpl(ProductRepository productRepository, MongoTemplate mongoTemplate) {
        this.productRepository = productRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public ProductDto getByName(final String name) {
        Product product = productRepository.findByName(name)
                .orElseThrow(() -> new DocumentNotFoundException("Product with name %s not found".formatted(name)));
        return toDto(product);
    }

    @Override
    public List<ProductDto> getByCategories(final String category) {
        List<Product> product = productRepository.findByCategoryName(category);

        if (product.isEmpty()) throw new DocumentNotFoundException("Product with category %s not found".formatted(category));
        return toDto(product);
    }

    @Override
    public ProductDto getByUuid(final String uuid) {
        Product product = productRepository.findByUuid(uuid)
                .orElseThrow(() -> new DocumentNotFoundException("Product with uuid %s not found".formatted(uuid)));
        return toDto(product);
    }

    @Override
    public String add(ProductDto productDto) {
        boolean productExists = productRepository.findByName(productDto.getName())
                .isPresent();

        if (productExists) throw new DocumentAlreadyExistsException("Product with name %s already exists"
                .formatted(productDto.getName()));

        Product product = build(productDto);
        return productRepository.save(product).getUuid();
    }

    @Override
    public void update(final ProductDto productDto) {
        boolean exists = productRepository.existsByUuid(productDto.getUuid());

        if (!exists) throw new DocumentNotFoundException("Product with uuid %s not found".formatted(productDto.getUuid()));

        QueryAndUpdate queryAndUpdate = getQueryAndUpdate(productDto);

        mongoTemplate.updateFirst(queryAndUpdate.query(), queryAndUpdate.update(), Product.class);
    }

    @Override
    public void updatePriceAndStock(ProductPriceAndStockDto productPriceAndStockDto) {
        boolean exists = productRepository.existsByUuid(productPriceAndStockDto.getUuid());

        if (!exists)
            throw new DocumentNotFoundException("Product with uuid %s not found".formatted(productPriceAndStockDto.getUuid()));

        final double MINIMUM_PRICE = 0.0;
        final int MINIMUM_STOCK = 0;

        if (productPriceAndStockDto.getStock() < MINIMUM_STOCK && productPriceAndStockDto.getPrice().doubleValue() < MINIMUM_PRICE) {
            throw new InvalidDataException("Stock and Price need to be 0 or greater");
        }

        Query query = new Query(Criteria.where("uuid").is(productPriceAndStockDto.getUuid()));
        Update update = new Update();

        if (productPriceAndStockDto.getStock() >= MINIMUM_STOCK)
            update.set("stock", productPriceAndStockDto.getStock());

        if (productPriceAndStockDto.getPrice().doubleValue() >= MINIMUM_PRICE)
            update.set("price", productPriceAndStockDto.getPrice());

        mongoTemplate.updateFirst(query, update, Product.class);
    }

    private QueryAndUpdate getQueryAndUpdate(ProductDto productDto) {
        Query query = new Query(Criteria.where("uuid").is(productDto.getUuid()));
        Update update = new Update()
                .set("name", productDto.getName())
                .set("description", productDto.getDescription())
                .set("isActive", productDto.isActive())
                .set("categories", productDto.getCategories().stream()
                        .map(categoryDto -> Category
                                .builder()
                                .name(categoryDto.getName())
                                .description(categoryDto.getDescription())
                                .isActive(categoryDto.isActive())
                                .build())
                        .toList())
                .set("images", productDto.getImages().stream()
                        .map(imageDto -> Image
                                .builder()
                                .imagePath(imageDto.getImagePath())
                                .isMain(imageDto.isMain())
                                .build())
                        .toList());
        return new QueryAndUpdate(query, update);
    }

    private record QueryAndUpdate(Query query, Update update) {
    }

    private Product build(final ProductDto productDto) {
        return Product
                .builder()
                .uuid(UUID.randomUUID().toString())
                .name(productDto.getName())
                .description(productDto.getDescription())
                .price(productDto.getPrice())
                .stock(productDto.getStock())
                .isActive(productDto.isActive())
                .categories(productDto.getCategories().stream()
                        .map(categoryDto -> Category
                                .builder()
                                .name(categoryDto.getName())
                                .description(categoryDto.getDescription())
                                .isActive(categoryDto.isActive())
                                .build())
                        .toList())
                .images(productDto.getImages().stream()
                        .map(imageDto -> Image
                                .builder()
                                .imagePath(imageDto.getImagePath())
                                .isMain(imageDto.isMain())
                                .build())
                        .toList())
                .build();
    }

    private List<ProductDto> toDto(final List<Product> products) {
        return products.stream()
                .map(this::toDto)
                .toList();
    }

    private ProductDto toDto(final Product product) {
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
