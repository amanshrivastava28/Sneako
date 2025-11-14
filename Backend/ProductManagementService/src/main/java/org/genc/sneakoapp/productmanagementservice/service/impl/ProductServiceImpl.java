package org.genc.sneakoapp.productmanagementservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.genc.sneakoapp.productmanagementservice.dto.ProductDTO;
import org.genc.sneakoapp.productmanagementservice.entity.Category;
import org.genc.sneakoapp.productmanagementservice.entity.Product;
import org.genc.sneakoapp.productmanagementservice.exception.ProductNotFoundException;
import org.genc.sneakoapp.productmanagementservice.exception.InsufficientStockException;
import org.genc.sneakoapp.productmanagementservice.repo.ProductRepository;
import org.genc.sneakoapp.productmanagementservice.service.api.CategoryService;
import org.genc.sneakoapp.productmanagementservice.service.api.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    @Override
    public ProductDTO createProduct(ProductDTO productdto) {
        Product productEntity = getProductDetails(productdto);
        Product productObj = productRepository.save(productEntity);
        log.info("Created a Product with the id: {}", productObj.getProductID());
        return mapProductEntityDTO(productObj);
    }

    @Override
    public Page<ProductDTO> getProduct(Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(this::mapProductEntityDTO);
    }

    @Override
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product productEntity = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product Not Found with ID: " + id));

        if (productDTO.getProductName() != null) {
            productEntity.setProductName(productDTO.getProductName());
        }
        if (productDTO.getImageUrl() != null) {
            productEntity.setImageUrl(productDTO.getImageUrl());
        }
        if (productDTO.getDescription() != null) {
            productEntity.setDescription(productDTO.getDescription());
        }
        if (productDTO.getStockQuantity() != null) {
            productEntity.setStockQuantity(productDTO.getStockQuantity());
        }
        if (productDTO.getPrice() != null) {
            productEntity.setPrice(productDTO.getPrice());
        }
        if (productDTO.getCategoryName() != null) {
            Category category = categoryService.findByCategoryEntityByName(productDTO.getCategoryName());
            productEntity.setCategory(category);
        }

        Product updatedProduct = productRepository.save(productEntity);
        log.info("Updated product with ID: {} and category: {}", updatedProduct.getProductID(),
                updatedProduct.getCategory().getName());

        return mapProductEntityDTO(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product Not Found with ID: " + id));
        log.info("Product with the id: {} deleted", product.getProductID());
        productRepository.delete(product);
    }

    @Override
    public ProductDTO findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product Not Found with ID: " + id));
        return mapProductEntityDTO(product);
    }

    @Override
    public Long totalProduct() {
        return productRepository.count();
    }

    public ProductDTO mapProductEntityDTO(Product productObj) {
        return new ProductDTO(productObj.getProductID(),
                productObj.getImageUrl(),
                productObj.getProductName(),
                productObj.getDescription(),
                productObj.getPrice(),
                productObj.getStockQuantity(),
                productObj.getCategory().getName());
    }

    private Product getProductDetails(ProductDTO productDTO) {
        Product productObj = Product.builder()
                .productName(productDTO.getProductName())
                .description(productDTO.getDescription())
                .imageUrl(productDTO.getImageUrl())
                .price(productDTO.getPrice())
                .stockQuantity(productDTO.getStockQuantity())
                .build();

        if (productDTO.getCategoryName() != null) {
            Category categoryEntity = categoryService.findByCategoryEntityByName(productDTO.getCategoryName());
            productObj.setCategory(categoryEntity);
        }
        return productObj;
    }

    @Override
    @Transactional
    public void reduceStock(Long productId, Long quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product Not Found with ID: " + productId));

        Long currentStock = product.getStockQuantity();
        if (currentStock == null || currentStock < quantity) {
            throw new InsufficientStockException("Insufficient stock for product ID: " + productId);
        }

        product.setStockQuantity(currentStock - quantity);
        product.setUpdatedBy("FRONTEND");
        productRepository.save(product);
    }
}
