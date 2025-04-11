package io.github.jotabrc.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.jotabrc.dto.AddProductDto;
import io.github.jotabrc.dto.ProductDto;
import io.github.jotabrc.dto.ProductPriceDto;
import io.github.jotabrc.ov_kafka_cp.Topic;
import io.github.jotabrc.ov_kafka_cp.broker.Producer;
import io.github.jotabrc.response.ResponseBody;
import io.github.jotabrc.response.ResponsePayload;
import io.github.jotabrc.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static io.github.jotabrc.controller.ControllerDefaults.MAPPING_PREFIX;
import static io.github.jotabrc.controller.ControllerDefaults.MAPPING_VERSION_SUFFIX;

@RequestMapping(MAPPING_PREFIX + MAPPING_VERSION_SUFFIX + "/product")
@RestController
@Tag(name = "Product Controller", description = "Manage Product operations")
public class ProductController {

    private final ProductService productService;
    private final Producer producer;

    @Autowired
    public ProductController(ProductService productService, Producer producer) {
        this.productService = productService;
        this.producer = producer;
    }

    @PostMapping("/add")
    public ResponseEntity<ResponsePayload<ProductDto>> add(@RequestBody final AddProductDto addProductDto)
            throws NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        ProductDto productDto = productService.add(addProductDto);
        URI location = ServletUriComponentsBuilder
                .fromPath(MAPPING_PREFIX + MAPPING_VERSION_SUFFIX + "/product/uuid/{uuid}")
                .buildAndExpand(productDto.getUuid())
                .toUri();
        producer.producer(productDto, "localhost:9092" , Topic.INVENTORY_ADD_ITEM.getTopic());
        return ResponseEntity.created(location).body(new ResponsePayload<ProductDto>()
                .setMessage("Product inserted with uuid %s".formatted(productDto.getUuid())));
    }

    @GetMapping("/get/name/{name}")
    public ResponseEntity<ResponsePayload<ProductDto>> getByName(@PathVariable("name") final String name) {
        ProductDto productDto = productService.getByName(name);
        return ResponseEntity.ok(new ResponsePayload<ProductDto>()
                .setMessage("Product found")
                .setBody(new ResponseBody<ProductDto>()
                        .setData(List.of(productDto)))
        );
    }

    @GetMapping("/get/category/{category}")
    public ResponseEntity<ResponsePayload<ProductDto>> getByCategory(@PathVariable("category") final String category) {
        List<ProductDto> productDto = productService.getByCategories(category);
        return ResponseEntity.ok(
                new ResponsePayload<ProductDto>()
                        .setMessage("Product found")
                        .setBody(new ResponseBody<ProductDto>()
                                .setData(productDto))
        );
    }

    @GetMapping("/get/uuid/{uuid}")
    public ResponseEntity<ResponsePayload<ProductDto>> getByUuid(@PathVariable("uuid") final String uuid) {
        ProductDto productDto = productService.getByUuid(uuid);
        return ResponseEntity.ok(new ResponsePayload<ProductDto>()
                .setMessage("Product found")
                .setBody(new ResponseBody<ProductDto>()
                        .setData(List.of(productDto)))
        );
    }

    @PutMapping("/update")
    public ResponseEntity<ResponsePayload<ProductDto>> update(@RequestBody final ProductDto productDto) {
        productService.update(productDto);
        return ResponseEntity
                .ok(
                        new ResponsePayload<ProductDto>()
                                .setMessage("Product updated")
                );
    }

    @PutMapping("/update/price")
    public ResponseEntity<ResponsePayload<ProductDto>> updatePrice(
            @RequestBody final ProductPriceDto productPriceDto
    ) {
        productService.updatePrice(productPriceDto);
        return ResponseEntity
                .ok(
                        new ResponsePayload<ProductDto>()
                                .setMessage("Product price updated")
                );
    }
}
