package org.genc.sneakoapp.productmanagementservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.genc.sneakoapp.productmanagementservice.dto.ProductDTO;
import org.genc.sneakoapp.productmanagementservice.entity.Category;
import org.genc.sneakoapp.productmanagementservice.entity.Product;
import org.genc.sneakoapp.productmanagementservice.repo.ProductRepository;
import org.genc.sneakoapp.productmanagementservice.service.api.CategoryService;
import org.genc.sneakoapp.productmanagementservice.service.api.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    @Override
    public ProductDTO createProduct(ProductDTO productdto) {
        Product productEntity=getProductDetails(productdto);
        Product productObj=productRepository.save(productEntity);
        log.info("created a Employee with the id:{}",productObj.getProductID());
        return mapProductEntityDTO(productObj) ;
    }

    @Override
    public Page<ProductDTO> getProduct(Pageable pageable) {
        Page<Product> productPage=productRepository.findAll(pageable);
        return productPage.map(this::mapProductEntityDTO);
    }

    @Override
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product productEntity=productRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("Product " +
                "Not Found"));
        Category category=null;
        if(productDTO.getProductName()!=null){
            productEntity.setProductName(productDTO.getProductName());
        }
        if(productDTO.getImageUrl()!=null){
            productEntity.setImageUrl(productDTO.getImageUrl());
        }
        if(productDTO.getDescription()!=null){
            productEntity.setDescription(productDTO.getDescription());
        }
        if(productDTO.getStockQuantity()!=null){
            productEntity.setStockQuantity(productDTO.getStockQuantity());
        }
        if(productDTO.getPrice()!=null){
            productEntity.setPrice(productDTO.getPrice());
        }
        if(productDTO.getCategoryName()!=null){
            category=categoryService.findByCategoryEntityByName(productDTO.getCategoryName());
        }
        Product perProduct=productRepository.save(productEntity);
        return mapProductEntityDTO(productEntity);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product=productRepository.findById(id).orElseThrow(()->new RuntimeException("Not found"));
        log.info("prouduct with the id: {} deleted",product.getProductID());
        productRepository.delete(product);
    }

    @Override
    public ProductDTO findById(Long id) {
        Product product=productRepository.findById(id).orElseThrow(()->new RuntimeException("Not found"));
        return mapProductEntityDTO(product);
    }

    @Override
    public Long totalProduct() {
        return productRepository.count();
    }


    public ProductDTO mapProductEntityDTO(Product productObj)
    {
        return new ProductDTO(productObj.getProductID(),
                productObj.getImageUrl(),
                productObj.getProductName(),
                productObj.getDescription(),
                productObj.getPrice(),
                productObj.getStockQuantity(),
                productObj.getCategory().getName());
    }

    private Product getProductDetails(ProductDTO productDTO){
        Product productObj=Product.builder().productName(productDTO.getProductName())
                .description(productDTO.getDescription())
                .imageUrl(productDTO.getImageUrl())
                .price(productDTO.getPrice())
                .stockQuantity(productDTO.getStockQuantity()).build();
        Category categoryEntity=null;
        if((productDTO.getCategoryName()!=null)){
            categoryEntity=categoryService.findByCategoryEntityByName(productDTO.getCategoryName());

        }
        productObj.setCategory(categoryEntity);
        return productObj;
    }
}
