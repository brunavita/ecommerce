package co.uk.yapily.ecommerce.model;

import co.uk.yapily.ecommerce.exception.ProductException;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @NotNull(message = "Name cannot be null")
    @Size(max = 200, message = "Name cannot exceed 200 characters")
    private String name;

    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private double price;

    @Builder.Default
    private Date addedAt = Date.from(Instant.now());

    @ElementCollection(targetClass = Label.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "product_labels", joinColumns = @JoinColumn(name = "productId"))
    private List<Label> labels;

    public void setLabels(List<String> labels) {
        if (labels == null || labels.isEmpty()) {
            throw new ProductException("Labels cannot be null or empty");
        }

        this.labels = labels.stream()
                .map(String::toUpperCase)
                .map(Label::fromString)
                .collect(Collectors.toList());
    }
}