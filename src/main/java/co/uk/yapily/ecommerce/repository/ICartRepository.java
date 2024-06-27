package co.uk.yapily.ecommerce.repository;

import co.uk.yapily.ecommerce.model.Cart;
import co.uk.yapily.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByCartId(Long cartId);
}