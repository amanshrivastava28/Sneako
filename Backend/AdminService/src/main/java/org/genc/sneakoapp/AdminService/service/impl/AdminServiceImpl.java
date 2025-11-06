package org.genc.sneakoapp.AdminService.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.genc.sneakoapp.AdminService.dto.OrderDTO;
import org.genc.sneakoapp.AdminService.dto.OrderItemDTO;
import org.genc.sneakoapp.AdminService.dto.ProductDTO;
import org.genc.sneakoapp.AdminService.dto.UserDetailsDTO;
import org.genc.sneakoapp.AdminService.entity.Category;
import org.genc.sneakoapp.AdminService.entity.Order;
import org.genc.sneakoapp.AdminService.entity.Product;
import org.genc.sneakoapp.AdminService.entity.User;
import org.genc.sneakoapp.AdminService.enums.RoleType;
import org.genc.sneakoapp.AdminService.repo.CategoryRepository;
import org.genc.sneakoapp.AdminService.repo.OrderRepository;
import org.genc.sneakoapp.AdminService.repo.ProductRepository;
import org.genc.sneakoapp.AdminService.repo.UserRepository;
import org.genc.sneakoapp.AdminService.service.api.AdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Override
    public ProductDTO findById(Long id) {
        Product product=productRepository.findById(id).orElseThrow(()->new RuntimeException("Not found"));
        return mapProductEntityDTO(product);
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

    public ProductDTO createProduct(ProductDTO productdto) {
        Product productEntity=getProductDetails(productdto);
        Product productObj=productRepository.save(productEntity);
        log.info("created a Employee with the id:{}",productObj.getProductID());
        return mapProductEntityDTO(productObj) ;
    }

    private Product getProductDetails(ProductDTO productDTO){
        Product productObj=Product.builder().productName(productDTO.getProductName())
                .description(productDTO.getDescription())
                .imageUrl(productDTO.getImageUrl())
                .price(productDTO.getPrice())
                .stockQuantity(productDTO.getStockQuantity()).build();
        Category categoryEntity=null;
        if((productDTO.getCategoryName()!=null)){
            categoryEntity=findByCategoryEntityByName(productDTO.getCategoryName());

        }
        productObj.setCategory(categoryEntity);
        return productObj;
    }

    public Category findByCategoryEntityByName(String name) {
        Optional<Category> categoryOptional = categoryRepository.findByName(name);

        return categoryOptional
                .orElseThrow(() -> new RuntimeException("Category Not Found by name: " + name));
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
            category=findByCategoryEntityByName(productDTO.getCategoryName());
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
    public Long totalProduct() {
        return productRepository.count();
    }

    @Override
    public Page<OrderDTO> getOrders(Pageable pageable) {
        Page<Order> orderPage = orderRepository.findAll(pageable);
        return orderPage.map(this::mapOrderEntityDTO);
    }


    public OrderDTO mapOrderEntityDTO(Order orderObj) {
        List<OrderItemDTO> itemDTOs = orderObj.getOrderItems().stream()
                .map(item -> OrderItemDTO.builder()
                        .orderItemId(item.getOrderItemId())
                        .productId(item.getProductId())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .totalPrice(item.getTotalPrice())
                        .size(item.getSize())
                        .build())
                .toList();

        return OrderDTO.builder()
                .orderId(orderObj.getOrderId())
                .userId(orderObj.getUserId())
                .shippingAddress(orderObj.getShippingAddress())
                .orderStatus(orderObj.getOrderStatus())
                .totalPrice(orderObj.getTotalPrice())
                .orderDate(orderObj.getOrderDate())
                .orderItems(itemDTOs)
                .build();
    }

    @Override
    public List<UserDetailsDTO> getAllUsers() {
        return userRepository.findAllByRoleName(RoleType.ROLE_CUSTOMER).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    private UserDetailsDTO mapToDto(User u) {
        return UserDetailsDTO.builder()
                .id(u.getId())
                .username(u.getUsername())
                .email(u.getEmail())
                .build();
    }
    @Override
    @Transactional
    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User with id " + id + " not found");
        }
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public OrderDTO updateOrderStatus(Long orderId, String newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setOrderStatus(newStatus);
        Order updatedOrder = orderRepository.save(order);

        List<OrderItemDTO> itemDTOs = updatedOrder.getOrderItems().stream()
                .map(item -> OrderItemDTO.builder()
                        .orderItemId(item.getOrderItemId())
                        .productId(item.getProductId())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .totalPrice(item.getTotalPrice())
                        .size(item.getSize())
                        .build())
                .toList();

        return OrderDTO.builder()
                .orderId(updatedOrder.getOrderId())
                .userId(updatedOrder.getUserId())
                .shippingAddress(updatedOrder.getShippingAddress())
                .orderStatus(updatedOrder.getOrderStatus())
                .totalPrice(updatedOrder.getTotalPrice())
                .orderDate(updatedOrder.getOrderDate())
                .orderItems(itemDTOs)
                .build();
    }

    @Override
    public UserDetailsDTO findUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return UserDetailsDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    @Override
    public Long calculateTotalRevenue() {
        List<Order> allOrders = orderRepository.findAll();

        BigDecimal totalRevenue = allOrders.stream()
                .map(Order::getTotalPrice)
                .filter(price -> price != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalRevenue.longValue();
    }

    @Override
    public Long totalOrders() {
        return orderRepository.count();
    }

    @Override
    public Long TotalUsers() {
        return userRepository.countByRoleName(RoleType.ROLE_CUSTOMER);
    }



}
