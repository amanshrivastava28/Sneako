package org.genc.sneakoapp.productmanagementservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.genc.sneakoapp.productmanagementservice.dto.CategoryDTO;
import org.genc.sneakoapp.productmanagementservice.entity.Category;
import org.genc.sneakoapp.productmanagementservice.repo.CategoryRepository;
import org.genc.sneakoapp.productmanagementservice.service.api.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;


    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category categoryEntity=new Category();
        categoryEntity.setCategoryID(categoryDTO.getCategoryID());
        categoryEntity.setName(categoryDTO.getName());
        Category categoryObj=categoryRepository.save(categoryEntity);
        log.info("New Category creted with the :{}",categoryObj.getCategoryID());
        CategoryDTO responseDTO=new CategoryDTO(categoryObj.getCategoryID(),categoryObj.getName());
        return responseDTO;
    }

    @Override
    public CategoryDTO findById(Long id) {
        Category category=categoryRepository.findById(id).orElseThrow(()->new RuntimeException("Category Not Found"));

        CategoryDTO categoryDTO=new CategoryDTO(
                category.getCategoryID(),
                category.getName()
        );

        return categoryDTO;
    }

    @Override
    public Category findByCategoryEntityByName(String name) {
        Optional<Category> categoryOptional = categoryRepository.findByName(name);

        return categoryOptional
                .orElseThrow(() -> new RuntimeException("Category Not Found by name: " + name));
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(category -> CategoryDTO.builder()
                        .categoryID(category.getCategoryID())
                        .name(category.getName())
                        .build())
                .toList();
    }

}
