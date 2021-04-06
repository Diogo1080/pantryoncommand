package com.pantryoncommand.converters;

import com.pantryoncommand.command.recipe.CreateOrUpdateIngredientDto;
import com.pantryoncommand.command.recipe.IngredientDetailsDto;
import com.pantryoncommand.persistence.entity.IngredientEntity;

public class IngredientConverter {
    /**
     * From {@link CreateOrUpdateIngredientDto} to {@link IngredientEntity}
     *
     * @param createOrUpdateIngredientDto {@link CreateOrUpdateIngredientDto}
     * @return {@link IngredientEntity}
     */
    public static IngredientEntity fromCreateOrUpdateIngredientDtoToIngredientEntity(CreateOrUpdateIngredientDto createOrUpdateIngredientDto) {
        return IngredientEntity.builder()
                .name(createOrUpdateIngredientDto.getName())
                .build();
    }

    /**
     * From {@link IngredientEntity} to {@link IngredientDetailsDto}
     *
     * @param ingredientEntity {@link IngredientEntity}
     * @return {@link IngredientDetailsDto}
     */
    public static IngredientDetailsDto fromIngredientEntityToIngredientDetailsDto(IngredientEntity ingredientEntity) {
        return IngredientDetailsDto.builder()
                .id(ingredientEntity.getId())
                .categoryId(ingredientEntity.getCategoryEntity().getId())
                .name(ingredientEntity.getName())
                .build();
    }
}
