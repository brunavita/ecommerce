package co.uk.yapily.ecommerce.service;

import co.uk.yapily.ecommerce.model.Cart;
import co.uk.yapily.ecommerce.model.CartItem;
import co.uk.yapily.ecommerce.model.CheckoutResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ICartService {

    Cart createCart();

    List<Cart> getAllCarts();

    Cart modifyCart(Long id, List<CartItem> items);

    CheckoutResponse checkoutCart(Long id);
}