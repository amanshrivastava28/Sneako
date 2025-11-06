package org.genc.sneakoapp.productmanagementservice.service.api;



import org.genc.sneakoapp.productmanagementservice.dto.CategoryDTO;
import org.genc.sneakoapp.productmanagementservice.entity.Category;

import java.util.List;

public interface CategoryService {
    public CategoryDTO createCategory(CategoryDTO categoryDTO);

   public CategoryDTO findById(Long id);
   public Category findByCategoryEntityByName(String name);
   public List<CategoryDTO> getAllCategories();
}
