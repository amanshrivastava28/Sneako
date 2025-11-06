package org.genc.sneakoapp.cartmanagementservice.repo;

import org.genc.sneakoapp.cartmanagementservice.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCartItemIdAndProductId(Long cartitemId, Long productId);
    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.userId = :userId AND ci.productId = :productId AND ci.size = :size")
    List<CartItem> findExistingItems(@Param("userId") Long userId,
                                     @Param("productId") Long productId,
                                     @Param("size") Integer size);

}
