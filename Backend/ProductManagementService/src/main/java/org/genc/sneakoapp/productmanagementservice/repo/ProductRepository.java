package org.genc.sneakoapp.productmanagementservice.repo;

import org.genc.sneakoapp.productmanagementservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {

}
