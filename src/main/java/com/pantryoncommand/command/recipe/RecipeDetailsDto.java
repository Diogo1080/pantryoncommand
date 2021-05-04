package com.pantryoncommand.command.recipe;

import com.pantryoncommand.command.ingredient.IngredientDetailsDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * RecipeDetailsDto mainly used to store recipe info
 */

@Data
@Builder
public class RecipeDetailsDto {
    private long id;
    private long userId;
    private String name;
    private String steps;
    private String photo;
    private String prepTime;
    private List<IngredientDetailsDto> ingredientIds;
}
