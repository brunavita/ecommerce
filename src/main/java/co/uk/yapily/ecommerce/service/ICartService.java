package co.uk.yapily.ecommerce.service;

import co.uk.yapily.ecommerce.dto.CartDto;
import co.uk.yapily.ecommerce.dto.CheckoutResponseDto;
import co.uk.yapily.ecommerce.model.CartItem;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ICartService {

    CartDto createCart();

    List<CartDto> getAllCarts();

    CartDto modifyCart(Long id, List<CartItem> items);

    CheckoutResponseDto checkoutCart(Long id);
}