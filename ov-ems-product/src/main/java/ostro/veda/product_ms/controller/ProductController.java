package ostro.veda.product_ms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ostro.veda.product_ms.dto.ProductDto;
import ostro.veda.product_ms.response.ResponseBody;
import ostro.veda.product_ms.response.ResponsePayload;
import ostro.veda.product_ms.service.ProductServiceImpl;

import java.net.URI;
import java.util.List;

@RequestMapping("/api/v1/product")
@RestController
public class ProductController {

    @Autowired
    private ProductServiceImpl productService;

    @GetMapping("/{name}")
    public ResponseEntity<ResponsePayload<ProductDto>> getByName(@PathVariable("name") String name) {
        ProductDto productDto = productService.getByName(name);
        return ResponseEntity.ok(new ResponsePayload<ProductDto>()
                .setMessage("Product found")
                .setBody(new ResponseBody<ProductDto>()
                        .setData(List.of(productDto)))
        );
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ResponsePayload<ProductDto>> getByCategory(@PathVariable("category") String category) {
        List<ProductDto> productDto = productService.getByCategories(category);
        return ResponseEntity.ok(
                new ResponsePayload<ProductDto>()
                .setMessage("Product found")
                .setBody(new ResponseBody<ProductDto>()
                        .setData(productDto))
        );
    }

    @GetMapping("/uuid/{uuid}")
    public ResponseEntity<ResponsePayload<ProductDto>> getByUuid(@PathVariable("uuid") String uuid) {
        ProductDto productDto = productService.getByUuid(uuid);
        return ResponseEntity.ok(new ResponsePayload<ProductDto>()
                .setMessage("Product found")
                .setBody(new ResponseBody<ProductDto>()
                        .setData(List.of(productDto)))
        );
    }

    @PostMapping
    public ResponseEntity<ResponsePayload<ProductDto>> add(@RequestBody ProductDto productDto) {
        String uuid = productService.add(productDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/api/v1/product/uuid/{uuid}")
                .buildAndExpand(uuid)
                .toUri();
        return ResponseEntity.created(location).body(new ResponsePayload<ProductDto>()
                .setMessage("Product inserted with uuid %s".formatted(uuid)));
    }
}
