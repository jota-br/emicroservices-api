package ostro.veda.product_ms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ostro.veda.product_ms.dto.ProductDto;
import ostro.veda.product_ms.response.ResponsePayload;
import ostro.veda.product_ms.service.ProductServiceImpl;

@RequestMapping("/api/v1/product")
@RestController
public class ProductController {

    @Autowired
    private ProductServiceImpl productService;

    @GetMapping("/{name}")
    public ResponseEntity<ResponsePayload<ProductDto>> get(@PathVariable("name") String name) {
        ProductDto productDto = productService.get(name);
        return ResponseEntity.ok(new ResponsePayload<ProductDto>()
                .setMessage("Product found")
                .setBody(productDto));
    }

    @PostMapping
    public ResponseEntity<ResponsePayload<ProductDto>> add(@RequestBody ProductDto productDto) {
        productService.add(productDto);
        return ResponseEntity.ok(new ResponsePayload<ProductDto>()
                .setMessage("Product inserted"));
    }
}
