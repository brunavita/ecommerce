package co.uk.yapily.ecommerce.service;

import co.uk.yapily.ecommerce.dto.ProductDto;
import co.uk.yapily.ecommerce.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IProductService {

    List<ProductDto> getAllProducts();

    ProductDto getProductById(Long id);

    ProductDto createProduct(Product product);

    void deleteProduct(Long id);
}