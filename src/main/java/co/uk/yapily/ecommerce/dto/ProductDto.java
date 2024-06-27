package co.uk.yapily.ecommerce.dto;

import co.uk.yapily.ecommerce.model.Label;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    @NotNull(message = "Product ID cannot be null")
    private Long productId;

    @NotNull(message = "Product name cannot be null")
    @Size(max = 200, message = "Product name cannot exceed 200 characters")
    private String name;

    @NotNull(message = "Product price cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Product price must be greater than 0")
    private double price;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    @Builder.Default
    private Date addedAt = Date.from(Instant.now());

    private List<Label> labels;

    public static ProductDto fromModel(co.uk.yapily.ecommerce.model.Product product) {
        return ProductDto.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .price(product.getPrice())
                .addedAt(product.getAddedAt())
                .labels(product.getLabels())
                .build();
    }
}