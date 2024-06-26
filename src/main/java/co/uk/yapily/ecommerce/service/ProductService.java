package co.uk.yapily.ecommerce.service;

import co.uk.yapily.ecommerce.exception.ProductException;
import co.uk.yapily.ecommerce.model.Product;
import co.uk.yapily.ecommerce.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements IProductService{

    @Autowired
    private IProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product createProduct(Product product) {
        if (productRepository.findByName(product.getName()).isPresent()) {
            throw new ProductException("Product with the same name already exists.");
        }
        return productRepository.save(product);
    }


    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}