package org.genc.sneakoapp.AdminService.service.api;

import org.genc.sneakoapp.AdminService.dto.OrderDTO;
import org.genc.sneakoapp.AdminService.dto.ProductDTO;
import org.genc.sneakoapp.AdminService.dto.UserDetailsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminService {
    public ProductDTO findById(Long id);
    public ProductDTO createProduct(ProductDTO productDTO);
    public Page<ProductDTO> getProduct(Pageable pageable);
    public  ProductDTO updateProduct(Long id,ProductDTO productDTO);
    public void  deleteProduct(Long id);
    public Long totalProduct();
    public Page<OrderDTO> getOrders(Pageable pageable);
    public List<UserDetailsDTO> getAllUsers();
    public void deleteUserById(Long id);
    public OrderDTO updateOrderStatus(Long orderId, String newStatus);
    public  UserDetailsDTO findUserById(Long id);
    public Long calculateTotalRevenue();
    public Long totalOrders();
    public  Long TotalUsers();

}
