package co.uk.yapily.ecommerce.service;

import co.uk.yapily.ecommerce.dto.ProductDto;
import co.uk.yapily.ecommerce.exception.ProductException;
import co.uk.yapily.ecommerce.model.Product;
import co.uk.yapily.ecommerce.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService implements IProductService{

    @Autowired
    private IProductRepository productRepository;

    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(ProductDto::fromModel)
                .collect(Collectors.toList());
    }

    public ProductDto getProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (!product.isPresent()) {
            throw new ProductException("Product doesn't exist.");
        }
        return ProductDto.fromModel(product.get());
    }

    public ProductDto createProduct(Product product) {
        if (productRepository.findByName(product.getName()).isPresent()) {
            throw new ProductException("Product with the same name already exists.");
        }

        return ProductDto.fromModel(productRepository.save(product));
    }


    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}