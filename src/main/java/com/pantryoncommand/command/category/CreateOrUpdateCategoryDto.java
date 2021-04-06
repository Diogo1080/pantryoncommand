package com.pantryoncommand.command.category;

import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * CreateOrUpdateDetailsDto mainly used to create and update category in database
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CreateOrUpdateCategoryDto {

    @NotNull(message = "Category must have a name")
    private String categoryName;

}