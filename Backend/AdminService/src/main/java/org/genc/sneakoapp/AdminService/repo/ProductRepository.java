package org.genc.sneakoapp.AdminService.repo;


import org.genc.sneakoapp.AdminService.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {

}
