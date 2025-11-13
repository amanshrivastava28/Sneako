package org.genc.sneakoapp.AdminService.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.genc.sneakoapp.AdminService.dto.OrderDTO;

import org.genc.sneakoapp.AdminService.dto.ProductDTO;
import org.genc.sneakoapp.AdminService.dto.UserDetailsDTO;






import org.genc.sneakoapp.AdminService.service.api.AdminService;
import org.genc.sneakoapp.AdminService.util.PageResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {


    private final RestTemplate restTemplate;

    private final String PRODUCT_SERVICE_URL = "http://localhost:8095/api/v1/product-service/product";
    private final String ORDER_SERVICE_URL = "http://localhost:8090/api/v1/order-service/order";
    private final String USER_SERVICE_URL = "http://localhost:8092/api/v1/user-service/users";



    @Override
    public ProductDTO findById(Long id) {
        String url = PRODUCT_SERVICE_URL + "/" + id;
        return restTemplate.getForObject(url, ProductDTO.class);
    }


    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        String url = PRODUCT_SERVICE_URL;
        ProductDTO createdProduct = restTemplate.postForObject(url, productDTO, ProductDTO.class);
        log.info("Created product with id: {}", createdProduct.getProductID());
        return createdProduct;
    }


    @Override
    public Page<ProductDTO> getProduct(Pageable pageable) {
        String url = PRODUCT_SERVICE_URL + "?page=" + pageable.getPageNumber() + "&size=" + pageable.getPageSize();

        ResponseEntity<PageResponse<ProductDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PageResponse<ProductDTO>>() {}
        );

        PageResponse<ProductDTO> pageResponse = response.getBody();
        return new PageImpl<>(pageResponse.getContent(), pageable, pageResponse.getTotalElements());
    }



    @Override
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        String url = PRODUCT_SERVICE_URL + "/" + id;
        restTemplate.put(url, productDTO);
        return findById(id);
    }

    @Override
    public void deleteProduct(Long id) {
        String url = PRODUCT_SERVICE_URL + "/" + id;
        restTemplate.delete(url);
        log.info("Deleted product with id: {}", id);
    }


    @Override
    public Long totalProduct() {
        String url = PRODUCT_SERVICE_URL + "/totalproducts";
        Long totalProducts = restTemplate.getForObject(url, Long.class);
        log.info("Fetched total products from ProductService: {}", totalProducts);
        return totalProducts;
    }

    @Override
    public Page<OrderDTO> getOrders(Pageable pageable) {
        String url = ORDER_SERVICE_URL + "?page=" + pageable.getPageNumber() + "&size=" + pageable.getPageSize();

        ResponseEntity<PageResponse<OrderDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PageResponse<OrderDTO>>() {}
        );

        PageResponse<OrderDTO> pageResponse = response.getBody();
        return new PageImpl<>(pageResponse.getContent(), pageable, pageResponse.getTotalElements());
    }

    @Override
    public OrderDTO updateOrderStatus(Long orderId, String newStatus) {
        String url = ORDER_SERVICE_URL + "/" + orderId;
        OrderDTO request = new OrderDTO();
        request.setOrderStatus(newStatus);

        ResponseEntity<OrderDTO> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                new HttpEntity<>(request),
                OrderDTO.class
        );
        return response.getBody();
    }


    @Override
    public Long totalOrders() {
        String url = ORDER_SERVICE_URL + "/totalorders";
        return restTemplate.getForObject(url, Long.class);
    }

    @Override
    public Long calculateTotalRevenue() {
        String url = ORDER_SERVICE_URL + "/totalrevenue";
        return restTemplate.getForObject(url, Long.class);
    }
    @Override
    public List<UserDetailsDTO> getAllUsers() {
        String url = USER_SERVICE_URL;
        ResponseEntity<List<UserDetailsDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<UserDetailsDTO>>() {}
        );
        return response.getBody();
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        String url = USER_SERVICE_URL + "/" + id;
        restTemplate.delete(url);
        log.info("Deleted user with id: {}", id);
    }

    @Override
    public UserDetailsDTO findUserById(Long id) {
        String url = USER_SERVICE_URL + "/admin/" + id;
        return restTemplate.getForObject(url, UserDetailsDTO.class);
    }

    @Override
    public Long TotalUsers() {
        String url = USER_SERVICE_URL + "/totalusers";
        Long totalUsers = restTemplate.getForObject(url, Long.class);
        log.info("Fetched total users from UserService: {}", totalUsers);
        return totalUsers;
    }


//    @Override
//    public List<UserDetailsDTO> getAllUsers() {
//        return userRepository.findAllByRoleName(RoleType.ROLE_CUSTOMER).stream()
//                .map(this::mapToDto)
//                .collect(Collectors.toList());
//    }
//    private UserDetailsDTO mapToDto(User u) {
//        return UserDetailsDTO.builder()
//                .id(u.getId())
//                .username(u.getUsername())
//                .email(u.getEmail())
//                .build();
//    }
//    @Override
//    @Transactional
//    public void deleteUserById(Long id) {
//        if (!userRepository.existsById(id)) {
//            throw new IllegalArgumentException("User with id " + id + " not found");
//        }
//        userRepository.deleteById(id);
//    }
//
//    @Override
//    public UserDetailsDTO findUserById(Long id) {
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        return UserDetailsDTO.builder()
//                .id(user.getId())
//                .username(user.getUsername())
//                .email(user.getEmail())
//                .build();
//    }
//
//
//    @Override
//    public Long TotalUsers() {
//        return userRepository.countByRoleName(RoleType.ROLE_CUSTOMER);
//    }



}
