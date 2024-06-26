package co.uk.yapily.ecommerce.controller;

import co.uk.yapily.ecommerce.model.Label;
import co.uk.yapily.ecommerce.model.Product;
import co.uk.yapily.ecommerce.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    public void testGetAllProducts() throws Exception {
        List<Product> products = creatProductList();

        when(productService.getAllProducts()).thenReturn(products);

        mockMvc.perform(get("/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("Product1", "Product2")));
    }

    @Test
    public void testGetProductById() throws Exception {
        Product product1 = createProduct("Product1", 1L, 10.0, List.of(Label.FOOD));

        when(productService.getProductById(1L)).thenReturn(Optional.of(product1));

        mockMvc.perform(get("/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Product1"))
                .andExpect(jsonPath("$.price").value(10.0))
                .andExpect(jsonPath("$.addedAt").exists())
                .andExpect(jsonPath("$.labels[0]").value(Label.FOOD.toString()));
    }

    @Test
    public void testCreateProduct() throws Exception {
        Product product1 = createProduct("Product1", 1L, 10.0, List.of(Label.FOOD));

        when(productService.createProduct(any(Product.class))).thenReturn(product1);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(product1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Product1"))
                .andExpect(jsonPath("$.price").value(10.0))
                .andExpect(jsonPath("$.addedAt").exists())
                .andExpect(jsonPath("$.labels[0]").value(Label.FOOD.toString()));

        verify(productService, times(1)).createProduct(any(Product.class));
    }

    @Test
    public void testDeleteProduct() throws Exception {
        doNothing().when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(productService, times(1)).deleteProduct(1L);
    }

    private List<Product> creatProductList() {
        Product product1 = createProduct("Product1", 1L, 10.0, List.of(Label.FOOD));
        Product product2 = createProduct("Product2", 2L, 15.0, List.of(Label.DRINK));
        return List.of(product1, product2);
    }

    private Product createProduct(String name, Long id, double price, List<Label> labels) {
        return Product.builder()
                .productId(id)
                .name(name)
                .price(price)
                .labels(labels)
                .build();
    }
}