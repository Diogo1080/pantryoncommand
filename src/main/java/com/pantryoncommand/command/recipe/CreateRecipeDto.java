package com.pantryoncommand.command.recipe;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * createRecipeDto for creating or updating
 */
@Data
@Builder
public class CreateRecipeDto {
    @NotNull(message = "Must have a creator user")
    private long userId;

    @NotNull(message = "Must have a name")
    private String name;

    @NotNull(message = "Must have steps")
    private String steps;

    private String prepTime;

    @NotNull(message = "Must have ingredients")
    private List<Long> ingredientIds;
}
