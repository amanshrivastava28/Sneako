package org.genc.sneakoapp.AdminService;

import org.genc.sneakoapp.AdminService.dto.ProductDTO;
import org.genc.sneakoapp.AdminService.dto.UserDetailsDTO;
import org.genc.sneakoapp.AdminService.service.impl.AdminServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AdminServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AdminServiceImpl adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById_returnsProduct() {
        ProductDTO product = ProductDTO.builder()
                .productID(1L)
                .productName("Test Product")
                .description("desc")
                .price(new BigDecimal("10.00"))
                .build();

        when(restTemplate.getForObject("http://localhost:8095/api/v1/product-service/product/1", ProductDTO.class))
                .thenReturn(product);

        ProductDTO result = adminService.findById(1L);

        assertThat(result.getProductName()).isEqualTo("Test Product");
        verify(restTemplate, times(1)).getForObject(anyString(), eq(ProductDTO.class));
    }

    @Test
    void testCreateProduct_returnsCreatedProduct() {
        ProductDTO input = ProductDTO.builder()
                .productName("New Product")
                .description("desc")
                .price(new BigDecimal("5.00"))
                .build();

        ProductDTO saved = ProductDTO.builder()
                .productID(2L)
                .productName("New Product")
                .description("desc")
                .price(new BigDecimal("5.00"))
                .build();

        when(restTemplate.postForObject("http://localhost:8095/api/v1/product-service/product", input, ProductDTO.class))
                .thenReturn(saved);

        ProductDTO result = adminService.createProduct(input);

        assertThat(result.getProductID()).isEqualTo(2L);
        verify(restTemplate, times(1)).postForObject(anyString(), any(ProductDTO.class), eq(ProductDTO.class));
    }

    @Test
    void testUpdateProduct_callsPutAndReturnsUpdated() {
        ProductDTO updated = ProductDTO.builder()
                .productID(1L)
                .productName("Updated")
                .description("desc")
                .price(new BigDecimal("20.00"))
                .build();

        doNothing().when(restTemplate).put("http://localhost:8095/api/v1/product-service/product/1", updated);
        when(restTemplate.getForObject("http://localhost:8095/api/v1/product-service/product/1", ProductDTO.class))
                .thenReturn(updated);

        ProductDTO result = adminService.updateProduct(1L, updated);

        assertThat(result.getProductName()).isEqualTo("Updated");
        verify(restTemplate, times(1)).put(anyString(), any(ProductDTO.class));
        verify(restTemplate, times(1)).getForObject(anyString(), eq(ProductDTO.class));
    }

    @Test
    void testDeleteProduct_callsDelete() {
        doNothing().when(restTemplate).delete("http://localhost:8095/api/v1/product-service/product/1");

        adminService.deleteProduct(1L);

        verify(restTemplate, times(1)).delete(anyString());
    }

    @Test
    void testTotalProduct_returnsCount() {
        when(restTemplate.getForObject("http://localhost:8095/api/v1/product-service/product/totalproducts", Long.class))
                .thenReturn(5L);

        Long result = adminService.totalProduct();

        assertThat(result).isEqualTo(5L);
        verify(restTemplate, times(1)).getForObject(anyString(), eq(Long.class));
    }

    @Test
    void testGetAllUsers_returnsList() {
        List<UserDetailsDTO> users = Arrays.asList(
                UserDetailsDTO.builder().id(1L).username("Alice").email("alice@example.com").build(),
                UserDetailsDTO.builder().id(2L).username("Bob").email("bob@example.com").build()
        );

        ResponseEntity<List<UserDetailsDTO>> responseEntity = new ResponseEntity<>(users, HttpStatus.OK);

        when(restTemplate.exchange(eq("http://localhost:8092/api/v1/user-service/users"),
                eq(HttpMethod.GET),
                isNull(),
                ArgumentMatchers.<ParameterizedTypeReference<List<UserDetailsDTO>>>any()))
                .thenReturn(responseEntity);

        List<UserDetailsDTO> result = adminService.getAllUsers();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getUsername()).isEqualTo("Alice");
    }

    @Test
    void testFindUserById_returnsUser() {
        UserDetailsDTO user = UserDetailsDTO.builder()
                .id(1L)
                .username("Alice")
                .email("alice@example.com")
                .build();

        when(restTemplate.getForObject("http://localhost:8092/api/v1/user-service/users/admin/1", UserDetailsDTO.class))
                .thenReturn(user);

        UserDetailsDTO result = adminService.findUserById(1L);

        assertThat(result.getUsername()).isEqualTo("Alice");
        verify(restTemplate, times(1)).getForObject(anyString(), eq(UserDetailsDTO.class));
    }
}
