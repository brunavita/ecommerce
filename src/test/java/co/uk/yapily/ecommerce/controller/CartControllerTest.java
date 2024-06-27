package co.uk.yapily.ecommerce.controller;

import co.uk.yapily.ecommerce.dto.CartDto;
import co.uk.yapily.ecommerce.dto.CheckoutResponseDto;
import co.uk.yapily.ecommerce.model.*;
import co.uk.yapily.ecommerce.service.ICartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CartControllerTest {

    @Mock
    private ICartService cartService;

    @InjectMocks
    private CartController cartController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

        @Test
    public void testCreateCart() {
        CartDto mockCartDto = new CartDto();
        when(cartService.createCart()).thenReturn(mockCartDto);

        ResponseEntity<CartDto> response = cartController.createCart();

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockCartDto, response.getBody());
        verify(cartService, times(1)).createCart();
    }

    @Test
    public void testGetAllCarts() {
        List<CartDto> mockCarts = new ArrayList<>();
        mockCarts.add(new CartDto());
        mockCarts.add(new CartDto());
        when(cartService.getAllCarts()).thenReturn(mockCarts);

        List<CartDto> response = cartController.getAllCarts();

        assertEquals(2, response.size());
        verify(cartService, times(1)).getAllCarts();
    }

    @Test
    public void testModifyCart() {
        Long cartId = 1L;
        List<CartItem> items = new ArrayList<>();
        items.add(new CartItem());

        CartDto mockUpdatedCartDto = new CartDto();
        when(cartService.modifyCart(anyLong(), anyList())).thenReturn(mockUpdatedCartDto);

        ResponseEntity<CartDto> response = cartController.modifyCart(cartId, items);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUpdatedCartDto, response.getBody());
        verify(cartService, times(1)).modifyCart(eq(cartId), eq(items));
    }

    @Test
    public void testCheckoutCart() {
        Long cartId = 1L;

        CheckoutResponseDto mockCheckoutResponseDto = new CheckoutResponseDto();
        when(cartService.checkoutCart(anyLong())).thenReturn(mockCheckoutResponseDto);

        ResponseEntity<CheckoutResponseDto> response = cartController.checkoutCart(cartId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockCheckoutResponseDto, response.getBody());
        verify(cartService, times(1)).checkoutCart(eq(cartId));
    }
}