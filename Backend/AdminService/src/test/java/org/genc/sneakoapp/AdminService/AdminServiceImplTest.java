package org.genc.sneakoapp.AdminService;

import org.genc.sneakoapp.AdminService.dto.OrderDTO;
import org.genc.sneakoapp.AdminService.dto.OrderItemDTO;
import org.genc.sneakoapp.AdminService.dto.ProductDTO;
import org.genc.sneakoapp.AdminService.dto.UserDetailsDTO;
import org.genc.sneakoapp.AdminService.entity.Category;
import org.genc.sneakoapp.AdminService.entity.Order;
import org.genc.sneakoapp.AdminService.entity.OrderItem;
import org.genc.sneakoapp.AdminService.entity.Product;
import org.genc.sneakoapp.AdminService.entity.User;
import org.genc.sneakoapp.AdminService.enums.RoleType;
import org.genc.sneakoapp.AdminService.repo.CategoryRepository;
import org.genc.sneakoapp.AdminService.repo.OrderRepository;
import org.genc.sneakoapp.AdminService.repo.ProductRepository;
import org.genc.sneakoapp.AdminService.repo.UserRepository;
import org.genc.sneakoapp.AdminService.service.impl.AdminServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for AdminServiceImpl using Mockito (no Spring context).
 */
@ExtendWith(MockitoExtension.class)
public class AdminServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdminServiceImpl adminService;

    @BeforeEach
    void setUp() {
        // InjectMocks creates the service and injects mocks
    }

    @Test
    void findById_returnsMappedProductDTO() {
        // Arrange
        Category cat = new Category();
        cat.setName("Sneakers");
        Product product = new Product();
        product.setProductID(1L);
        product.setProductName("AirMax");
        product.setDescription("Cool shoes");
        product.setPrice(new BigDecimal("79.99"));
        product.setStockQuantity(10L);
        product.setImageUrl("http://img");
        product.setCategory(cat);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // Act
        ProductDTO dto = adminService.findById(1L);

        // Assert
        assertThat(dto).isNotNull();
        assertThat(dto.getProductID()).isEqualTo(1L);
        assertThat(dto.getProductName()).isEqualTo("AirMax");
        assertThat(dto.getCategoryName()).isEqualTo("Sneakers");
        verify(productRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void createProduct_savesAndReturnsProductDTO() {
        // Arrange: input DTO without ID, category name provided (note capital C)
        ProductDTO input = ProductDTO.builder()
                .productName("NewModel")
                .description("desc")
                .price(new BigDecimal("49.99"))
                .stockQuantity(5L)
                .imageUrl("url")
                .CategoryName("Sports") // use exact field name in DTO
                .build();

        Category category = new Category();
        category.setName("Sports");

        // saved entity returned by repository
        Product saved = new Product();
        saved.setProductID(2L);
        saved.setProductName("NewModel");
        saved.setDescription("desc");
        saved.setPrice(new BigDecimal("49.99"));
        saved.setStockQuantity(5L);
        saved.setImageUrl("url");
        saved.setCategory(category);

        when(categoryRepository.findByName("Sports")).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(saved);

        // Act
        ProductDTO result = adminService.createProduct(input);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getProductID()).isEqualTo(2L);
        assertThat(result.getProductName()).isEqualTo("NewModel");
        assertThat(result.getCategoryName()).isEqualTo("Sports");
        verify(categoryRepository, times(1)).findByName("Sports");
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void getProduct_returnsPageOfProductDTOs() {
        // Arrange
        Product p1 = new Product();
        p1.setProductID(1L);
        p1.setProductName("P1");
        p1.setCategory(new Category(){{
            setName("C1");
        }});

        List<Product> content = List.of(p1);
        Page<Product> page = new PageImpl<>(content);

        when(productRepository.findAll(any(Pageable.class))).thenReturn(page);

        // Act
        Page<ProductDTO> result = adminService.getProduct(Pageable.ofSize(10));

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getProductName()).isEqualTo("P1");
        verify(productRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void updateProduct_updatesFieldsAndReturnsDTO() {
        // Arrange existing entity
        Product existing = new Product();
        existing.setProductID(5L);
        existing.setProductName("Old");
        existing.setDescription("old-desc");
        existing.setPrice(new BigDecimal("10.00"));
        existing.setStockQuantity(1L);

        when(productRepository.findById(5L)).thenReturn(Optional.of(existing));
        // simulate category lookup
        Category cat = new Category();
        cat.setName("UpdatedCat");
        when(categoryRepository.findByName("UpdatedCat")).thenReturn(Optional.of(cat));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        // DTO with updates (note capital C in CategoryName)
        ProductDTO update = ProductDTO.builder()
                .productName("NewName")
                .description("new-desc")
                .price(new BigDecimal("15.00"))
                .stockQuantity(10L)
                .CategoryName("UpdatedCat") // match your DTO field name
                .build();

        // Act
        ProductDTO result = adminService.updateProduct(5L, update);

        // Assert
        assertThat(result.getProductName()).isEqualTo("NewName");
        assertThat(result.getDescription()).isEqualTo("new-desc");
        assertThat(result.getPrice()).isEqualByComparingTo(new BigDecimal("15.00"));
        assertThat(result.getStockQuantity()).isEqualTo(10L);
        assertThat(result.getCategoryName()).isEqualTo("UpdatedCat");

        verify(productRepository).findById(5L);
        verify(categoryRepository).findByName("UpdatedCat");
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void deleteProduct_deletesWhenFound() {
        // Arrange
        Product p = new Product();
        p.setProductID(10L);
        when(productRepository.findById(10L)).thenReturn(Optional.of(p));

        // Act
        adminService.deleteProduct(10L);

        // Assert
        verify(productRepository).findById(10L);
        verify(productRepository).delete(p);
    }

    @Test
    void totalProduct_returnsCount() {
        when(productRepository.count()).thenReturn(7L);
        Long count = adminService.totalProduct();
        assertThat(count).isEqualTo(7L);
        verify(productRepository).count();
    }

    @Test
    void getOrders_returnsPageMapped() {
        // Arrange order and items
        OrderItem item = new OrderItem();
        item.setOrderItemId(1L);
        item.setProductId(11L);
        item.setQuantity(2L); // adjust if your entity uses Long
        item.setUnitPrice(new BigDecimal("5.00"));
        item.setTotalPrice(new BigDecimal("10.00"));

        Order order = new Order();
        order.setOrderId(100L);
        order.setUserId(50L);
        order.setShippingAddress("addr");
        order.setOrderStatus("NEW");
        order.setTotalPrice(new BigDecimal("10.00"));
        order.setOrderDate(LocalDateTime.now());
        order.setOrderItems(List.of(item));

        Page<Order> page = new PageImpl<>(List.of(order));
        when(orderRepository.findAll(any(Pageable.class))).thenReturn(page);

        // Act
        Page<OrderDTO> result = adminService.getOrders(Pageable.ofSize(10));

        // Assert
        assertThat(result.getTotalElements()).isEqualTo(1);
        OrderDTO dto = result.getContent().get(0);
        assertThat(dto.getOrderId()).isEqualTo(100L);
        assertThat(dto.getOrderItems()).hasSize(1);
        OrderItemDTO itemDto = dto.getOrderItems().get(0);
        assertThat(itemDto.getProductId()).isEqualTo(11L);

        verify(orderRepository).findAll(any(Pageable.class));
    }

    @Test
    void getAllUsers_mapsCustomerUsers() {
        User u1 = new User();
        u1.setId(200L);
        u1.setUsername("user1");
        u1.setEmail("u1@example.com");
        // assume repository returns only ROLE_CUSTOMER
        when(userRepository.findAllByRoleName(RoleType.ROLE_CUSTOMER)).thenReturn(List.of(u1));

        List<UserDetailsDTO> users = adminService.getAllUsers();

        assertThat(users).hasSize(1);
        assertThat(users.get(0).getId()).isEqualTo(200L);
        assertThat(users.get(0).getUsername()).isEqualTo("user1");
        verify(userRepository).findAllByRoleName(RoleType.ROLE_CUSTOMER);
    }

    @Test
    void deleteUserById_whenNotExists_throws() {
        when(userRepository.existsById(999L)).thenReturn(false);
        assertThatThrownBy(() -> adminService.deleteUserById(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User with id 999 not found");
        verify(userRepository).existsById(999L);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void updateOrderStatus_updatesAndReturnsDTO() {
        // Arrange
        Order order = new Order();
        order.setOrderId(300L);
        order.setOrderStatus("PENDING");
        order.setUserId(20L);
        order.setShippingAddress("s");
        order.setTotalPrice(new BigDecimal("100.00"));
        order.setOrderDate(LocalDateTime.now());
        order.setOrderItems(List.of());

        when(orderRepository.findById(300L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        OrderDTO updated = adminService.updateOrderStatus(300L, "SHIPPED");

        // Assert
        assertThat(updated.getOrderStatus()).isEqualTo("SHIPPED");
        verify(orderRepository).findById(300L);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void findUserById_returnsUserDetailsDTO() {
        User user = new User();
        user.setId(400L);
        user.setUsername("alice");
        user.setEmail("alice@example.com");
        when(userRepository.findById(400L)).thenReturn(Optional.of(user));

        UserDetailsDTO dto = adminService.findUserById(400L);

        assertThat(dto.getId()).isEqualTo(400L);
        assertThat(dto.getUsername()).isEqualTo("alice");
        verify(userRepository).findById(400L);
    }

    @Test
    void calculateTotalRevenue_sumsOrderTotalPrice() {
        Order o1 = new Order();
        o1.setTotalPrice(new BigDecimal("10.50"));
        Order o2 = new Order();
        o2.setTotalPrice(new BigDecimal("5.25"));
        when(orderRepository.findAll()).thenReturn(List.of(o1, o2));

        Long total = adminService.calculateTotalRevenue();

        // 10.50 + 5.25 = 15.75 -> longValue = 15
        assertThat(total).isEqualTo(new BigDecimal("15.75").longValue());
        verify(orderRepository).findAll();
    }

    @Test
    void totalOrders_and_TotalUsers_returnCounts() {
        when(orderRepository.count()).thenReturn(13L);
        when(userRepository.countByRoleName(RoleType.ROLE_CUSTOMER)).thenReturn(42L);

        Long orders = adminService.totalOrders();
        Long users = adminService.TotalUsers();

        assertThat(orders).isEqualTo(13L);
        assertThat(users).isEqualTo(42L);

        verify(orderRepository).count();
        verify(userRepository).countByRoleName(RoleType.ROLE_CUSTOMER);
    }
}