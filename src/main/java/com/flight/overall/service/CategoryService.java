package com.flight.overall.service;

import com.flight.overall.dto.CategoriesDTO;
import com.flight.overall.dto.CategoryDTO;
import com.flight.overall.dto.CategoryGroupDTO;
import com.flight.overall.entity.Category;
import com.flight.overall.entity.CategoryGroup;
import com.flight.overall.mapper.EntityMapper;
import com.flight.overall.repository.CategoryGroupRepository;
import com.flight.overall.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryGroupRepository categoryGroupRepository;

    @Autowired
    private EntityMapper mapper;

    public List<Category> findAllCategories() {
        return categoryRepository.findCategories();
    }

    public List<CategoryGroup> findAllCategoryGroups() {
        return categoryGroupRepository.findCategoryGroups();
    }

    public CategoriesDTO getCategories() {
        List<Category> categories = findAllCategories();
        return mapper.toCategories(categories);
    }

    public List<CategoryGroupDTO> getCategoryGroups() {
        List<CategoryGroup> groups = findAllCategoryGroups();
        return mapper.toCategoryGroups(groups);
    }

    public void createCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        updateCategory(category, categoryDTO);
        categoryRepository.save(category);
    }

    public void saveCategories(CategoriesDTO categoriesDTO) {
        Map<Long, CategoryDTO> categoryById = new HashMap<>();
        for (CategoryDTO category : categoriesDTO.getCategories())
            categoryById.put(category.getId(), category);

        Iterable<Category> categories = categoryRepository.findAll();
        for (Category category : categories)
            updateCategory(category, categoryById.get(category.getId()));

        categoryRepository.saveAll(categories);
    }

    private void updateCategory(Category category, CategoryDTO categoryDTO) {
        Optional<CategoryGroup> categoryGroup = categoryGroupRepository.findById(categoryDTO.getCategoryGroup().getId());
        category.setTitle(categoryDTO.getTitle());
        category.setWeight(categoryDTO.getWeight());
        category.setDescription(categoryDTO.getDescription());
        categoryGroup.ifPresent(category::setCategoryGroup);
    }
}
