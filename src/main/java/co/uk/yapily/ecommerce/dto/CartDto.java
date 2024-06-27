package co.uk.yapily.ecommerce.dto;

import co.uk.yapily.ecommerce.model.Cart;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
    private Long cartId;
    private List<CartItemDto> cartItems;
    private boolean checkedOut;

    public static CartDto fromModel(Cart cart) {
        CartDto cartDto = CartDto.builder()
                .cartId(cart.getCartId())
                .checkedOut(cart.isCheckedOut())
                .build();

        if (cart.getCartItems() != null) {
            List<CartItemDto> itemDTOs = cart.getCartItems().stream()
                    .map(CartItemDto::fromModel)
                    .collect(Collectors.toList());
            cartDto.setCartItems(itemDTOs);
        }

        return cartDto;
    }
}

