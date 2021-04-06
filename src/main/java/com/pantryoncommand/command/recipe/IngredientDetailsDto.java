package com.pantryoncommand.command.recipe;


import lombok.Builder;
import lombok.Data;

/**
 * IngredientDetailsDto mainly used to handle ingredient data
 */

@Data
@Builder
public class IngredientDetailsDto {

    private long id;

    private long categoryId;

    private String name;

}