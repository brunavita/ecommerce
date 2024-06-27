package co.uk.yapily.ecommerce.controller;

import co.uk.yapily.ecommerce.dto.CartDto;
import co.uk.yapily.ecommerce.dto.CheckoutResponseDto;
import co.uk.yapily.ecommerce.model.CartItem;
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
    public ResponseEntity<CartDto> createCart() {
        CartDto cartDto = cartService.createCart();
        return ResponseEntity.status(201).body(cartDto);
    }

    @GetMapping
    public List<CartDto> getAllCarts() {
        return cartService.getAllCarts();
    }

    @PutMapping("/{cartId}")
    public ResponseEntity<CartDto> modifyCart(@PathVariable Long cartId, @RequestBody List<CartItem> items) {
        CartDto updatedCartDto = cartService.modifyCart(cartId, items);
        return ResponseEntity.ok(updatedCartDto);
    }

    @PostMapping("/{cartId}/checkout")
    public ResponseEntity<CheckoutResponseDto> checkoutCart(@PathVariable Long cartId) {
        CheckoutResponseDto checkedOutCartDto = cartService.checkoutCart(cartId);
        return ResponseEntity.ok(checkedOutCartDto);
    }
}