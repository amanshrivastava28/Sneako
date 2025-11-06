package org.genc.sneakoapp.productmanagementservice.dto;


import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CategoryDTO {

    @Nullable
    private Long categoryID;

    @NotBlank(message = "Enter the category name")
    private String name;


}
