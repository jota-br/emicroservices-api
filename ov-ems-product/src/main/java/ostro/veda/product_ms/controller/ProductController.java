package ostro.veda.product_ms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ostro.veda.product_ms.dto.ProductDto;
import ostro.veda.product_ms.dto.ProductPriceAndStockDto;
import ostro.veda.product_ms.response.ResponseBody;
import ostro.veda.product_ms.response.ResponsePayload;
import ostro.veda.product_ms.service.ProductService;

import java.net.URI;
import java.util.List;

import static ostro.veda.product_ms.controller.ControllerDefaults.MAPPING_PREFIX;
import static ostro.veda.product_ms.controller.ControllerDefaults.MAPPING_VERSION_SUFFIX;

@RequestMapping(MAPPING_PREFIX + MAPPING_VERSION_SUFFIX + "/product")
@RestController
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/add")
    public ResponseEntity<ResponsePayload<ProductDto>> add(@RequestBody final ProductDto productDto) {
        String uuid = productService.add(productDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/uuid/{uuid}")
                .buildAndExpand(uuid)
                .toUri();
        return ResponseEntity.created(location).body(new ResponsePayload<ProductDto>()
                .setMessage("Product inserted with uuid %s".formatted(uuid)));
    }

    @GetMapping("/{name}")
    public ResponseEntity<ResponsePayload<ProductDto>> getByName(@PathVariable("name") final String name) {
        ProductDto productDto = productService.getByName(name);
        return ResponseEntity.ok(new ResponsePayload<ProductDto>()
                .setMessage("Product found")
                .setBody(new ResponseBody<ProductDto>()
                        .setData(List.of(productDto)))
        );
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ResponsePayload<ProductDto>> getByCategory(@PathVariable("category") final String category) {
        List<ProductDto> productDto = productService.getByCategories(category);
        return ResponseEntity.ok(
                new ResponsePayload<ProductDto>()
                        .setMessage("Product found")
                        .setBody(new ResponseBody<ProductDto>()
                                .setData(productDto))
        );
    }

    @GetMapping("/uuid/{uuid}")
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
                .status(HttpStatus.NO_CONTENT)
                .body(
                        new ResponsePayload<ProductDto>()
                                .setMessage("Product updated")
                );
    }

    @PutMapping("/update/priceAndStock/{uuid}")
    public ResponseEntity<ResponsePayload<ProductDto>> updatePriceAndStock(
            @RequestBody final ProductPriceAndStockDto productPriceAndStockDto
    ) {
        productService.updatePriceAndStock(productPriceAndStockDto);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(
                        new ResponsePayload<ProductDto>()
                                .setMessage("Product stock and price updated")
                );
    }
}
