package com.pantryoncommand.command.category;

import lombok.Builder;
import lombok.Data;

/**
 * CategoryDetailsDto mainly used to handle category data
 */

@Data
@Builder
public class CategoryDetailsDto {
    private long id;

    private String name;
}