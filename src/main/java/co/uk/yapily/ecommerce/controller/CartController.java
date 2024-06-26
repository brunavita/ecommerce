package co.uk.yapily.ecommerce.controller;

import co.uk.yapily.ecommerce.exception.CartException;
import co.uk.yapily.ecommerce.model.Cart;
import co.uk.yapily.ecommerce.model.CartItem;
import co.uk.yapily.ecommerce.model.CheckoutResponse;
import co.uk.yapily.ecommerce.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private ICartService cartService;

    @PostMapping
    public ResponseEntity<Cart> createCart() {
        Cart cart = cartService.createCart();
        return ResponseEntity.status(201).body(cart);
    }

    @GetMapping
    public List<Cart> getAllCarts() {
        return cartService.getAllCarts();
    }

    @PutMapping("/{cartId}")
    public ResponseEntity<Cart> modifyCart(@PathVariable Long cartId, @RequestBody List<CartItem> items) {
        try {
            Cart updatedCart = cartService.modifyCart(cartId, items);
            return ResponseEntity.ok(updatedCart);
        } catch (CartException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{cartId}/checkout")
    public ResponseEntity<CheckoutResponse> checkoutCart(@PathVariable Long cartId) {
        try {
            CheckoutResponse checkedOutCart = cartService.checkoutCart(cartId);
            return ResponseEntity.ok(checkedOutCart);
        } catch (CartException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}