package co.uk.yapily.ecommerce.service;

import co.uk.yapily.ecommerce.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface IProductService {

    List<Product> getAllProducts();

    Optional<Product> getProductById(Long id);

    Product createProduct(Product product);

    void deleteProduct(Long id);
}