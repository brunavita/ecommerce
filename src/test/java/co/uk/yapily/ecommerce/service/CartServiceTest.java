package co.uk.yapily.ecommerce.service;

import co.uk.yapily.ecommerce.dto.CartDto;
import co.uk.yapily.ecommerce.dto.CheckoutResponseDto;
import co.uk.yapily.ecommerce.exception.CartException;
import co.uk.yapily.ecommerce.exception.ProductException;
import co.uk.yapily.ecommerce.model.Cart;
import co.uk.yapily.ecommerce.model.CartItem;
import co.uk.yapily.ecommerce.model.Product;
import co.uk.yapily.ecommerce.repository.ICartRepository;
import co.uk.yapily.ecommerce.repository.IProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


public class CartServiceTest {

    @Mock
    private ICartRepository cartRepository;

    @Mock
    private IProductRepository productRepository;

    @InjectMocks
    private CartService cartService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateCart() {
        Cart mockCart = new Cart();
        mockCart.setCartId(1L);
        when(cartRepository.save(any(Cart.class))).thenReturn(mockCart);

        CartDto result = cartService.createCart();

        assertNotNull(result);
        assertEquals(mockCart.getCartId(), result.getCartId());
    }

    @Test
    public void testGetAllCarts() {
        List<Cart> mockCarts = new ArrayList<>();
        mockCarts.add(new Cart());
        mockCarts.add(new Cart());
        when(cartRepository.findAll()).thenReturn(mockCarts);

        List<CartDto> result = cartService.getAllCarts();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testModifyCart() {
        Long cartId = 1L;
        List<CartItem> items = new ArrayList<>();
        CartItem item = new CartItem();
        item.setProductId(1L);
        item.setQuantity(2);
        items.add(item);

        Cart mockCart = new Cart();
        mockCart.setCartId(cartId);
        when(cartRepository.findById(eq(cartId))).thenReturn(Optional.of(mockCart));
        when(cartRepository.save(any(Cart.class))).thenReturn(mockCart);

        Product mockProduct = new Product();
        mockProduct.setProductId(1L);
        mockProduct.setPrice(10.0);
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(mockProduct));

        CartDto result = cartService.modifyCart(cartId, items);

        assertNotNull(result);
        assertEquals(cartId, result.getCartId());
        assertEquals(1, result.getCartItems().size());
        assertEquals(item.getProductId(), result.getCartItems().get(0).getProductId());
        assertEquals(item.getQuantity(), result.getCartItems().get(0).getQuantity());
    }

    @Test
    public void testModifyCart_CartNotFound() {
        Long cartId = 1L;
        List<CartItem> items = new ArrayList<>();

        when(cartRepository.findById(eq(cartId))).thenReturn(Optional.empty());

        assertThrows(CartException.class, () -> cartService.modifyCart(cartId, items));
    }

    @Test
    public void testCheckoutCart() {
        Long cartId = 1L;

        Cart mockCart = new Cart();
        mockCart.setCartId(cartId);
        mockCart.setCheckedOut(false);
        when(cartRepository.findById(eq(cartId))).thenReturn(Optional.of(mockCart));
        when(cartRepository.save(any(Cart.class))).thenReturn(mockCart);

        Product mockProduct = new Product();
        mockProduct.setProductId(1L);
        mockProduct.setPrice(10.0);
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(mockProduct));


        List<CartItem> cartItems = new ArrayList<>();
        CartItem cartItem = new CartItem().builder().productId(1L).quantity(2).cart(mockCart).build();
        cartItems.add(cartItem);

        mockCart.setCartItems(cartItems);

        CheckoutResponseDto result = cartService.checkoutCart(cartId);

        assertNotNull(result);
        assertEquals(cartId, result.getCart().getCartId());
        assertTrue(result.getCart().isCheckedOut());
        assertEquals(20.0, result.getTotalCost());
    }

    @Test
    public void testCheckoutCart_CartNotFound() {
        Long cartId = 1L;

        when(cartRepository.findById(eq(cartId))).thenReturn(Optional.empty());

        assertThrows(CartException.class, () -> cartService.checkoutCart(cartId));
    }

    @Test
    public void testCheckoutCart_ProductNotFound() {
        Long cartId = 1L;

        Cart mockCart = new Cart();
        mockCart.setCartId(cartId);
        mockCart.setCheckedOut(false);
        CartItem mockCartItem = new CartItem();
        mockCartItem.setProductId(1L);
        mockCartItem.setQuantity(1);
        mockCart.getCartItems().add(mockCartItem);
        when(cartRepository.findById(eq(cartId))).thenReturn(Optional.of(mockCart));
        when(productRepository.findById(eq(1L))).thenReturn(Optional.empty());

        assertThrows(ProductException.class, () -> cartService.checkoutCart(cartId));
    }
}