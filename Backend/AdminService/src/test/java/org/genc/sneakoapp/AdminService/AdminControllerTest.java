package org.genc.sneakoapp.AdminService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.genc.sneakoapp.AdminService.controller.AdminController;
import org.genc.sneakoapp.AdminService.dto.ProductDTO;
import org.genc.sneakoapp.AdminService.service.api.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class AdminControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    void setup() {
        // standaloneSetup avoids starting the Spring context and avoids using @MockBean
        mockMvc = MockMvcBuilders.standaloneSetup(adminController)
                .build();
    }

    @Test
    void testFindById_returnsProduct() throws Exception {
        // Arrange
        ProductDTO product = ProductDTO.builder()
                .productID(1L)
                .productName("Sample product")
                .description("desc")
                .price(new BigDecimal("9.99"))
                .build();

        when(adminService.findById(1L)).thenReturn(product);

        // Act & Assert
        mockMvc.perform(get("/api/v1/analytics-service/admin/product/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$.productName").value("Sample product"));

        verify(adminService, times(1)).findById(1L);
        verifyNoMoreInteractions(adminService);
    }

    @Test
    void testCreateProduct_returnsCreatedProduct() throws Exception {
        // Arrange
        ProductDTO input = ProductDTO.builder()
                .productName("New Product")
                .description("new desc")
                .price(new BigDecimal("5.00"))
                .build();

        ProductDTO saved = ProductDTO.builder()
                .productID(2L)
                .productName("New Product")
                .description("new desc")
                .price(new BigDecimal("5.00"))
                .build();

        when(adminService.createProduct(any(ProductDTO.class))).thenReturn(saved);

        // Act & Assert
        mockMvc.perform(post("/api/v1/analytics-service/admin/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$.productID").value(2))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$.productName").value("New Product"));

        verify(adminService, times(1)).createProduct(any(ProductDTO.class));
        verifyNoMoreInteractions(adminService);
    }
}