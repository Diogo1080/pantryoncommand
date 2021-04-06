package com.pantryoncommand.converters;

import com.pantryoncommand.command.category.CategoryDetailsDto;
import com.pantryoncommand.command.category.CreateOrUpdateCategoryDto;
import com.pantryoncommand.persistence.entity.CategoryEntity;

public class CategoryConverter {
    /**
     * From {@link CreateOrUpdateCategoryDto} to {@link CategoryEntity}
     *
     * @param createOrUpdateCategoryDto {@link CreateOrUpdateCategoryDto}
     * @return {@link CategoryEntity}
     */
    public static CategoryEntity fromCreateOrUpdateCategoryDtoToCategoryEntity(CreateOrUpdateCategoryDto createOrUpdateCategoryDto) {
        return CategoryEntity.builder()
                .name(createOrUpdateCategoryDto.getCategoryName())
                .build();
    }

    /**
     * From {@link CategoryEntity} to {@link CategoryDetailsDto}
     *
     * @param categoryEntity {@link CategoryEntity}
     * @return {@link CategoryDetailsDto}
     */
    public static CategoryDetailsDto fromCategoryEntityToCategoryDetailsDto(CategoryEntity categoryEntity) {
        return CategoryDetailsDto.builder()
                .id(categoryEntity.getId())
                .name(categoryEntity.getName())
                .build();
    }
}
