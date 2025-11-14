package org.genc.sneakoapp.AdminService.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {

    @Nullable
    private Long productID;

    @NotNull(message = "Image URL cant be null")
    private String imageUrl;

    @NotBlank(message = "Enter the Product name")
    private String productName;

    @NotNull(message = "Enter the Product description")
    private String description;

    @NotNull(message = "Enter the appropriate price")
    private BigDecimal price;

    @NotNull(message = "Enter the Stock quantity")
    private Long stockQuantity;

    @NotNull
    private  String categoryName;

}
