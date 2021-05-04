package com.pantryoncommand.converters;

import com.pantryoncommand.command.ingredient.IngredientDetailsDto;
import com.pantryoncommand.command.recipe.CreateRecipeDto;
import com.pantryoncommand.command.recipe.UpdateRecipeDto;
import com.pantryoncommand.command.recipe.RecipeDetailsDto;
import com.pantryoncommand.persistence.entity.RecipeEntity;
import com.pantryoncommand.persistence.entity.UserEntity;

import java.util.List;

public class RecipeConverter {
    public static RecipeEntity fromCreateRecipeDtoToRecipeEntity(CreateRecipeDto recipe, UserEntity userEntity) {

        return RecipeEntity.builder()
                .userEntity(userEntity)
                .name(recipe.getName())
                .prepTime(recipe.getPrepTime())
                .steps(recipe.getSteps())
                .build();
    }


    public static RecipeDetailsDto fromRecipeEntityToRecipeDetailsDto(
            RecipeEntity recipeEntity,
            List<IngredientDetailsDto> ingredientList) {

        return RecipeDetailsDto.builder()
                .id(recipeEntity.getRecipeId())
                .userId(recipeEntity.getUserEntity().getId())
                .name(recipeEntity.getName())
                .prepTime(recipeEntity.getPrepTime())
                .steps(recipeEntity.getSteps())
                .photo(recipeEntity.getPhoto())
                .ingredientIds(ingredientList)
                .build();
    }
}
