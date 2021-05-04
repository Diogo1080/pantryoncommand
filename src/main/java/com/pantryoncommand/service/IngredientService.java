package com.pantryoncommand.service;

import com.pantryoncommand.command.Paginated;
import com.pantryoncommand.command.ingredient.CreateOrUpdateIngredientDto;
import com.pantryoncommand.command.ingredient.IngredientDetailsDto;
import com.pantryoncommand.exeption.DatabaseCommunicationException;
import com.pantryoncommand.exeption.category.CategoryNotFoundException;
import com.pantryoncommand.exeption.ingredient.IngredientAlreadyExistsException;
import com.pantryoncommand.exeption.ingredient.IngredientNotFoundException;
import org.springframework.data.domain.Pageable;

/**
 * Common interface for Ingredient service, provides methods to manage Ingredients
 */
public interface IngredientService {
        /**
         * Create new ingredient
         * @param ingredientDetails {@link IngredientDetailsDto}
         * @return {@link IngredientDetailsDto}
         * @throws CategoryNotFoundException when ingredient is not found in database
         * @throws IngredientAlreadyExistsException when the ingredient already exists
         * @throws DatabaseCommunicationException when database connection isn't established
         */
        IngredientDetailsDto addNewIngredient(CreateOrUpdateIngredientDto ingredientDetails)
                throws  CategoryNotFoundException,
                        IngredientAlreadyExistsException,
                        DatabaseCommunicationException;

        /**
         * Get given ingredient with id
         * @param ingredientId Receives ingredient id
         * @return {@link IngredientDetailsDto}
         * @throws IngredientNotFoundException when ingredient is not found
         */
        IngredientDetailsDto getIngredientById(long ingredientId) throws IngredientNotFoundException;


        /**
         * Gets list of categories from database with pagination
         * @param pagination the page and number of elements per page
         * @return {@link Paginated <IngredientDetailsDto>}
         */
        Paginated<IngredientDetailsDto> getIngredientList(Pageable pagination);

        /**
         * Deletes certain Ingredient with id
         * @param ingredientId Receives ingredient id
         * @throws IngredientNotFoundException when ingredient is not found
         * @throws DatabaseCommunicationException when database connection isn't established
         */
        void deleteIngredient(long ingredientId) throws IngredientNotFoundException, DatabaseCommunicationException;

        /**
         * Update ingredient
         * @param ingredientId Receives ingredient is
         * @param updateIngredient Receives ingredient info
         * @return {@link IngredientDetailsDto}
         * @throws CategoryNotFoundException when ingredient is not found in database
         * @throws IngredientNotFoundException when ingredient is not found
         * @throws DatabaseCommunicationException when database connection isn't established
         */
        IngredientDetailsDto updateIngredient(long ingredientId, CreateOrUpdateIngredientDto updateIngredient)
                throws  CategoryNotFoundException,
                        IngredientNotFoundException,
                        DatabaseCommunicationException;
}
