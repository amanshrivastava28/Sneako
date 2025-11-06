package org.genc.sneakoapp.productmanagementservice.service.api;


import org.genc.sneakoapp.productmanagementservice.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    public ProductDTO createProduct(ProductDTO productdto);
    public Page<ProductDTO> getProduct(Pageable pageable);
    public  ProductDTO updateProduct(Long id,ProductDTO productDTO);
    public void  deleteProduct(Long id);
    public ProductDTO findById(Long id);
    public Long totalProduct();

}
