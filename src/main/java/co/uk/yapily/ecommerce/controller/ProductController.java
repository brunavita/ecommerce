package co.uk.yapily.ecommerce.controller;

import co.uk.yapily.ecommerce.dto.ProductDto;
import co.uk.yapily.ecommerce.model.Product;
import co.uk.yapily.ecommerce.service.IProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private IProductService productService;

    @GetMapping
    public List<ProductDto> getAllProducts() {

        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        ProductDto productDto = productService.getProductById(id);
        return ResponseEntity.status(200).body(productDto);
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody Product product) {
            ProductDto newProductDto = productService.createProduct(product);
            return ResponseEntity.status(201).body(newProductDto);
      }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}