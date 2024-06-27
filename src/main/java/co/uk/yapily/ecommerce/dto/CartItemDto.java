package co.uk.yapily.ecommerce.dto;

import co.uk.yapily.ecommerce.model.CartItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {
    private Long cartItemId;

    private Long productId;

    private int quantity;

    private Long cartId;

    public static CartItemDto fromModel(CartItem cartItem) {
        return CartItemDto.builder()
                .cartItemId(cartItem.getCartItemId())
                .productId(cartItem.getProductId())
                .quantity(cartItem.getQuantity())
                .cartId(cartItem.getCart().getCartId())
                .build();
    }

}
