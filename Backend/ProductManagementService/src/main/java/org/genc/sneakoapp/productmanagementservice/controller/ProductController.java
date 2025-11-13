package org.genc.sneakoapp.productmanagementservice.controller;


import com.netflix.discovery.EurekaClient;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.genc.sneakoapp.productmanagementservice.dto.ProductDTO;
import org.genc.sneakoapp.productmanagementservice.service.api.ProductService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product-service/product")
public class ProductController {
    private final Environment environment;
    @Value("${eureka.instance.instance-id}")
    private String instanceId;
    @Value("${spring.application.name}")
    private String appName;
    private final EurekaClient eurekaClient;
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productdto)
    {
        ProductDTO responseDTO=productService.createProduct(productdto);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public  ResponseEntity<Page<ProductDTO>> getProducts(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10")int size){
        Pageable pageable= PageRequest.of(page, size);
        Page<ProductDTO> ProductDTOPage=productService.getProduct(pageable);
        return  new ResponseEntity<>(ProductDTOPage,HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct( @PathVariable Long id,
                                                    @RequestBody ProductDTO productDTO){
        ProductDTO respdto=productService.updateProduct(id,productDTO);
        return new ResponseEntity<>(respdto,HttpStatus.OK);

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();

    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO>  findById( @PathVariable  Long id){
        return new ResponseEntity<>(productService.findById(id),HttpStatus.OK);
    }
    
    @PatchMapping("/{id}/stock")
    public ResponseEntity<Void> reduceStock(@PathVariable Long id, @RequestParam Long quantity) {
        productService.reduceStock(id, quantity);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/totalproducts")
    public ResponseEntity<Long> totalProducts() {
        Long totalProducts = productService.totalProduct();
        return new ResponseEntity<>(totalProducts, HttpStatus.OK);
    }


}
