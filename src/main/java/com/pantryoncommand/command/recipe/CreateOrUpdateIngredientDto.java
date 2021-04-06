package com.pantryoncommand.command.recipe;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Create Or Update Ingredients Dto mainly used to create or update ingredient data on database
 */

@Data
@Builder
public class CreateOrUpdateIngredientDto {
    @NotNull(message = "Must have a category")
    private long categoryId;

    @NotBlank(message = "Must have a name")
    private String name;
}
