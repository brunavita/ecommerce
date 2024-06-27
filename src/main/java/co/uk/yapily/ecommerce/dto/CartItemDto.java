package co.uk.yapily.ecommerce.dto;

import co.uk.yapily.ecommerce.model.CartItem;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Product cannot be null")
    private Long productId;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1, message = "Quantity must be greater than 0")
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
