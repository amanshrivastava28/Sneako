package org.genc.sneakoapp.AdminService.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.genc.sneakoapp.AdminService.dto.AnalyticsDTO;
import org.genc.sneakoapp.AdminService.dto.OrderDTO;
import org.genc.sneakoapp.AdminService.dto.ProductDTO;
import org.genc.sneakoapp.AdminService.dto.UserDetailsDTO;
import org.genc.sneakoapp.AdminService.service.api.AdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/analytics-service/admin")
@RequiredArgsConstructor
public class AdminController {
   private final AdminService adminService;


    @GetMapping("/product/{id}")
    public ResponseEntity<ProductDTO>  findById(@PathVariable  Long id){
        return new ResponseEntity<>(adminService.findById(id),HttpStatus.OK);
    }


    @PostMapping("/product")
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productdto)
    {
        ProductDTO responseDTO=adminService.createProduct(productdto);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/product")
    public ResponseEntity<Page<ProductDTO>> getProducts(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10")int size){
        Pageable pageable= PageRequest.of(page, size);
        Page<ProductDTO> productDTOPage=adminService.getProduct(pageable);
        return  new ResponseEntity<>(productDTOPage, HttpStatus.OK);
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<ProductDTO> updateProduct( @PathVariable Long id,
                                                     @RequestBody ProductDTO productDTO){
        ProductDTO respdto=adminService.updateProduct(id,productDTO);
        return new ResponseEntity<>(respdto,HttpStatus.OK);

    }
    @DeleteMapping("/product/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        adminService.deleteProduct(id);
        return ResponseEntity.noContent().build();

    }
    @GetMapping("/orders")
    public  ResponseEntity<Page<OrderDTO>> getOrders(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10")int size){
        Pageable pageable= PageRequest.of(page, size);
        Page<OrderDTO> OrderDTOPage=adminService.getOrders(pageable);
        return  new ResponseEntity<>(OrderDTOPage,HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDetailsDTO>> getAllUsers() {
        List<UserDetailsDTO> users = adminService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDetailsDTO> findUserById(@PathVariable Long id) {
        UserDetailsDTO user = adminService.findUserById(id);
        return ResponseEntity.ok(user);
    }



    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        adminService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("orders/{id}")
    public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable Long id, @RequestBody OrderDTO updatedOrder) {
        OrderDTO updated = adminService.updateOrderStatus(id, updatedOrder.getOrderStatus());
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @GetMapping("/analytics")
    public  ResponseEntity<AnalyticsDTO>  calculateAnayltics(){
        Long totalRevenue=adminService.calculateTotalRevenue();
        Long totalusers=adminService.TotalUsers();
        Long totalOrders= adminService.totalOrders();
        Long totalProducts=adminService.totalProduct();

        AnalyticsDTO analyticsDTO = new AnalyticsDTO(totalRevenue, totalOrders,totalusers,totalProducts);

        return ResponseEntity.ok(analyticsDTO);

    }

}
