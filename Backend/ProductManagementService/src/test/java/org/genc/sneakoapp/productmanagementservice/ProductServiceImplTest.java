package org.genc.sneakoapp.productmanagementservice;

import org.genc.sneakoapp.productmanagementservice.dto.ProductDTO;
import org.genc.sneakoapp.productmanagementservice.entity.Category;
import org.genc.sneakoapp.productmanagementservice.entity.Product;
import org.genc.sneakoapp.productmanagementservice.exception.ProductNotFoundException;
import org.genc.sneakoapp.productmanagementservice.exception.InsufficientStockException;
import org.genc.sneakoapp.productmanagementservice.repo.ProductRepository;
import org.genc.sneakoapp.productmanagementservice.service.api.CategoryService;
import org.genc.sneakoapp.productmanagementservice.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private Category category;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        category = Category.builder().categoryID(1L).name("Shoes").build();

        product = Product.builder()
                .productID(1L)
                .productName("Test Product")
                .description("desc")
                .imageUrl("http://image.png")
                .price(BigDecimal.valueOf(100))
                .stockQuantity(10L)
                .category(category)
                .build();
    }

    @Test
    void testCreateProduct_success() {
        ProductDTO dto = new ProductDTO(null, "http://image.png", "New Product", "desc",
                BigDecimal.valueOf(50), 5L, "Shoes");

        when(categoryService.findByCategoryEntityByName("Shoes")).thenReturn(category);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDTO result = productService.createProduct(dto);

        assertThat(result.getProductName()).isEqualTo("Test Product");
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testUpdateProduct_success() {
        ProductDTO dto = new ProductDTO(null, "http://new.png", "Updated Product", "new desc",
                BigDecimal.valueOf(200), 20L, "Shoes");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(categoryService.findByCategoryEntityByName("Shoes")).thenReturn(category);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDTO result = productService.updateProduct(1L, dto);

        assertThat(result.getProductName()).isEqualTo("Updated Product"); // ✅ expect updated value
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testUpdateProduct_notFound_throwsException() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class,
                () -> productService.updateProduct(99L, new ProductDTO()));
    }

    @Test
    void testDeleteProduct_success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).delete(product);
    }

    @Test
    void testDeleteProduct_notFound_throwsException() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(99L));
    }

    @Test
    void testFindById_success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductDTO result = productService.findById(1L);

        assertThat(result.getProductName()).isEqualTo("Test Product");
    }

    @Test
    void testFindById_notFound_throwsException() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.findById(99L));
    }

    @Test
    void testTotalProduct_returnsCount() {
        when(productRepository.count()).thenReturn(5L);

        Long result = productService.totalProduct();

        assertThat(result).isEqualTo(5L);
    }

    @Test
    void testReduceStock_success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        productService.reduceStock(1L, 5L);

        assertThat(product.getStockQuantity()).isEqualTo(5L);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testReduceStock_insufficientStock_throwsException() {
        product.setStockQuantity(2L);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertThrows(InsufficientStockException.class, () -> productService.reduceStock(1L, 5L));
    }

    @Test
    void testReduceStock_productNotFound_throwsException() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.reduceStock(99L, 1L));
    }
}
