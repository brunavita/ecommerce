package co.uk.yapily.ecommerce.service;

import co.uk.yapily.ecommerce.dto.ProductDto;
import co.uk.yapily.ecommerce.model.Label;
import co.uk.yapily.ecommerce.model.Product;
import co.uk.yapily.ecommerce.repository.IProductRepository;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ProductServiceTest {

    @Mock
    private IProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllProducts() {
        List<Product> mockProducts = new ArrayList<>();
        mockProducts.add(new Product(1L, "Product 1", 10.0, Date.from(Instant.now()), List.of(Label.FOOD, Label.DRINK)));
        mockProducts.add(new Product(2L, "Product 2", 20.0, Date.from(Instant.now()), List.of(Label.FOOD)));
        when(productRepository.findAll()).thenReturn(mockProducts);

        List<ProductDto> result = productService.getAllProducts();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testGetProductById() {
        Long productId = 1L;
        Product mockProduct = new Product(productId, "Product 1", 10.0, Date.from(Instant.now()), List.of(Label.CLOTHES));
        when(productRepository.findById(productId)).thenReturn(Optional.of(mockProduct));

        ProductDto result = productService.getProductById(productId);

        assertNotNull(result);
        assertEquals(productId, result.getProductId());
    }

    @Test
    public void testCreateProduct() {
        Product newProduct = new Product(null, "New Product", 15.0, Date.from(Instant.now()), List.of(Label.LIMITED));
        when(productRepository.findByName(newProduct.getName())).thenReturn(Optional.empty());
        when(productRepository.save(newProduct)).thenReturn(new Product(1L, "New Product", 15.0, Date.from(Instant.now()), List.of(Label.LIMITED)));

        ProductDto result = productService.createProduct(newProduct);

        assertNotNull(result);
        assertEquals("New Product", result.getName());
    }

    @Test
    public void testDeleteProduct() {
        Long productId = 1L;

        productService.deleteProduct(productId);

        verify(productRepository, times(1)).deleteById(productId);
    }
}