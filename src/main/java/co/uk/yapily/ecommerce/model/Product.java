package co.uk.yapily.ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
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

    @Size(max = 200)
    private String name;

    private double price;

    @Builder.Default
    private Date addedAt = Date.from(Instant.now());

    @ElementCollection(targetClass = Label.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "product_labels", joinColumns = @JoinColumn(name = "productId"))
    private List<Label> labels;

    public void setLabels(List<String> labels) {
        if (labels == null || labels.isEmpty()) {
            throw new IllegalArgumentException("Labels cannot be null or empty");
        }

        this.labels = labels.stream()
                .map(String::toUpperCase)
                .map(Label::fromString)
                .collect(Collectors.toList());
    }
}