package co.uk.yapily.ecommerce;

import co.uk.yapily.ecommerce.controller.CartController;
import co.uk.yapily.ecommerce.controller.ProductController;
import co.uk.yapily.ecommerce.dto.CartDto;
import co.uk.yapily.ecommerce.dto.CartItemDto;
import co.uk.yapily.ecommerce.dto.CheckoutResponseDto;
import co.uk.yapily.ecommerce.dto.ProductDto;
import co.uk.yapily.ecommerce.model.CartItem;
import co.uk.yapily.ecommerce.model.Label;
import co.uk.yapily.ecommerce.model.Product;
import co.uk.yapily.ecommerce.service.ICartService;
import co.uk.yapily.ecommerce.service.IProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({ProductController.class, CartController.class})
@AutoConfigureMockMvc
public class IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IProductService productService;

    @MockBean
    private ICartService cartService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCompleteIntegration() throws Exception {
        Product product1 = createProduct("Product 1", 1L, 10.0, List.of(Label.CLOTHES));
        Product product2 = createProduct("Product 2", 2L, 20.0, List.of(Label.FOOD, Label.DRINK));

        List<ProductDto> products = Arrays.asList(ProductDto.fromModel(product1), ProductDto.fromModel(product2));

        Mockito.when(productService.getAllProducts()).thenReturn(products);
        Mockito.when(productService.getProductById(1L)).thenReturn(ProductDto.fromModel(product1));
        Mockito.when(productService.getProductById(2L)).thenReturn(ProductDto.fromModel(product2));

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product2)))
                .andExpect(status().isCreated());

        CartDto cartDto = CartDto.builder().cartId(1L).checkedOut(false).build();

        Mockito.when(cartService.createCart()).thenReturn(cartDto);

        mockMvc.perform(post("/carts"))
                .andExpect(status().isCreated());

        CartItem cartItem1 = CartItem.builder().productId(1L).quantity(2).build();
        CartItem cartItem2 = CartItem.builder().productId(2L).quantity(1).build();


        List<CartItem> cartItems = Arrays.asList(cartItem1, cartItem2);

        mockMvc.perform(put("/carts/{cartId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartItems)))
                .andExpect(status().isOk());

        cartDto.setCheckedOut(true);
        CheckoutResponseDto checkoutResponseDto = CheckoutResponseDto.builder().cart(cartDto).totalCost(50.0).build();

        Mockito.when(cartService.checkoutCart(1L)).thenReturn(checkoutResponseDto);

        mockMvc.perform(post("/carts/{cartId}/checkout", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.cart.cartId").value(1L))
                .andExpect(jsonPath("$.totalCost").value(50.0))
                .andExpect(jsonPath("$.cart.checkedOut").value(true));
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