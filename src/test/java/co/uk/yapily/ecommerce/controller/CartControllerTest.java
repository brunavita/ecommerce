package co.uk.yapily.ecommerce.controller;

import co.uk.yapily.ecommerce.model.*;
import co.uk.yapily.ecommerce.service.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Collections;
import java.util.List;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CartControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();
    }

    @Test
    public void testCreateCart() throws Exception {
        Cart newCart = createCart(1L, false, Collections.emptyList());

        when(cartService.createCart()).thenReturn(newCart);

        mockMvc.perform(post("/carts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.checkedOut").value(false))
                .andExpect(jsonPath("$.cartItems").isArray())
                .andExpect(jsonPath("$.cartItems[0].productId").doesNotHaveJsonPath())
                .andExpect(jsonPath("$.cartItems[0].quantity").doesNotHaveJsonPath());

        verify(cartService, times(1)).createCart();
    }

    @Test
    public void testGetAllCarts() throws Exception {
        Cart cart1 = createCartWithItem(1L, 1L, 1L, 2);
        Cart cart2 = createCartWithItem(2L, 2L, 2L, 1);
        List<Cart> carts = List.of(cart1, cart2);

        when(cartService.getAllCarts()).thenReturn(carts);

        mockMvc.perform(get("/carts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].checkedOut", containsInAnyOrder(false, false)))
                .andExpect(jsonPath("$[*].cartItems[0].productId", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].cartItems[0].quantity", containsInAnyOrder(2, 1)));

        verify(cartService, times(1)).getAllCarts();
    }

    @Test
    public void testModifyCart() throws Exception {
        CartItem newItem = createCartItem(null, 3L, 5);
        Cart modifiedCart = createCart(1L, false, List.of(newItem));

        when(cartService.modifyCart(eq(1L), eq(List.of(newItem)))).thenReturn(modifiedCart);

        mockMvc.perform(put("/carts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(Collections.singletonList(newItem))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.checkedOut").value(false))
                .andExpect(jsonPath("$.cartItems").isArray())
                .andExpect(jsonPath("$.cartItems[0].productId").value(3))
                .andExpect(jsonPath("$.cartItems[0].quantity").value(5));

        verify(cartService, times(1)).modifyCart(eq(1L), eq(List.of(newItem)));
    }

    @Test
    public void testCheckoutCart() throws Exception {
        Cart cart = createCartWithItem(1L, 1L, 1L, 2);
        cart.setCheckedOut(true);
        CheckoutResponse checkoutResponse = CheckoutResponse.builder()
                .cart(cart)
                .totalCost(20)
                .build();

        when(cartService.checkoutCart(1L)).thenReturn(checkoutResponse);

        mockMvc.perform(post("/carts/1/checkout")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cart.checkedOut").value(true))
                .andExpect(jsonPath("$.cart.cartItems").isArray())
                .andExpect(jsonPath("$.cart.cartItems[0].productId").value(1))
                .andExpect(jsonPath("$.cart.cartItems[0].quantity").value(2))
                .andExpect(jsonPath("$.totalCost").value(20.0));

        verify(cartService, times(1)).checkoutCart(1L);
    }

    private Cart createCart(Long cartId, boolean checkedOut, List<CartItem> cartItems) {
        return Cart.builder()
                .cartId(cartId)
                .checkedOut(checkedOut)
                .cartItems(cartItems)
                .build();
    }

    private Cart createCartWithItem(Long cartId, Long cartItemId, Long productId, int quantity) {
        Cart cart = createCart(cartId, false, Collections.emptyList());
        CartItem cartItem = createCartItem(cartItemId, productId, quantity);
        cart.setCartItems(List.of(cartItem));
        return cart;
    }

    private CartItem createCartItem(Long cartItemId, Long productId, int quantity) {
        return CartItem.builder()
                .cartItemId(cartItemId)
                .productId(productId)
                .quantity(quantity)
                .build();
    }
}