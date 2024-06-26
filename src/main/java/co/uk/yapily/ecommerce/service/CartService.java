package co.uk.yapily.ecommerce.service;

import co.uk.yapily.ecommerce.exception.CartException;
import co.uk.yapily.ecommerce.exception.ProductException;
import co.uk.yapily.ecommerce.model.Cart;
import co.uk.yapily.ecommerce.model.CartItem;
import co.uk.yapily.ecommerce.model.CheckoutResponse;
import co.uk.yapily.ecommerce.model.Product;
import co.uk.yapily.ecommerce.repository.ICartRepository;
import co.uk.yapily.ecommerce.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CartService implements ICartService {

    @Autowired
    private ICartRepository cartRepository;

    @Autowired
    private IProductRepository productRepository;

    public Cart createCart() {
        Cart cart = Cart.builder()
                .checkedOut(false)
                .build();

        return cartRepository.save(cart);
    }

    public List<Cart> getAllCarts() {
        return cartRepository.findAll();
    }

    public Cart modifyCart(Long cartId, List<CartItem> itemRequests) {
        Optional<Cart> optionalCart = cartRepository.findById(cartId);
        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
             cart.getCartItems().clear();

            for (CartItem itemRequest : itemRequests) {
                Product product = productRepository.findById(itemRequest.getProductId())
                        .orElseThrow(() -> new ProductException(String.format("Product with ID %s not found", itemRequest.getProductId())));

                CartItem cartItem = CartItem.builder()
                        .cart(cart)
                        .productId(product.getProductId())
                        .quantity(itemRequest.getQuantity())
                        .build();

                cart.getCartItems().add(cartItem);
            }

            return cartRepository.save(cart);
        } else {
            throw new CartException(String.format("Cart with ID: %s not found", cartId));
        }
    }

    public CheckoutResponse checkoutCart(Long cartId) {
        Optional<Cart> optionalCart = cartRepository.findById(cartId);
        if (optionalCart.isPresent()) {
            Cart checkedOutCart = optionalCart.get();
            checkedOutCart.setCheckedOut(true);
            cartRepository.save(checkedOutCart);

            double totalCost = checkedOutCart.getCartItems().stream()
                    .mapToDouble(item -> {
                        Product product = productRepository.findById(item.getProductId())
                                .orElseThrow(() -> new ProductException(String.format("Product with ID %s not found", item.getProductId())));
                        return product.getPrice() * item.getQuantity();
                    }).sum();

            return CheckoutResponse.builder()
                    .cart(checkedOutCart)
                    .totalCost(totalCost)
                    .build();
        } else {
            throw new CartException(String.format("Cart with ID: %s not found", cartId));
        }
    }
}