package com.pantryoncommand.service;

import com.pantryoncommand.command.Paginated;
import com.pantryoncommand.command.ingredient.IngredientListDto;
import com.pantryoncommand.command.recipe.CreateRecipeDto;
import com.pantryoncommand.command.recipe.UpdateRecipeDto;
import com.pantryoncommand.command.recipe.RecipeDetailsDto;
import com.pantryoncommand.exeption.DatabaseCommunicationException;
import com.pantryoncommand.exeption.file.FileNotAnImageException;
import com.pantryoncommand.exeption.file.FileNotFoundException;
import com.pantryoncommand.exeption.file.FileNotSavedException;
import com.pantryoncommand.exeption.file.ReadingFileException;
import com.pantryoncommand.exeption.ingredient.IngredientNotFoundException;
import com.pantryoncommand.exeption.recipe.RecipeNotFoundException;
import com.pantryoncommand.exeption.user.UserNotFoundException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Common interface for Recipe service, provides methods to manage Recipes
 */

public interface RecipeService {
    /**
     * Create new recipe
     *
     * @param createRecipeDto {@link CreateRecipeDto}
     * @return {@link RecipeDetailsDto}
     * @throws UserNotFoundException when user is not found
     * @throws IngredientNotFoundException when ingredient is not found
     * @throws DatabaseCommunicationException when database communication isn't established
     */
    RecipeDetailsDto addNewRecipe(CreateRecipeDto createRecipeDto) throws
            UserNotFoundException,
            IngredientNotFoundException,
            DatabaseCommunicationException;

    /**
     * Add image to recipe
     *
     * @param multipartImage the image
     * @param recipeId the id
     * @return {@link RecipeDetailsDto}
     * @throws RecipeNotFoundException when recipe is not found
     * @throws FileNotSavedException when file is not saved
     */
    RecipeDetailsDto addImageToRecipe(MultipartFile multipartImage, long recipeId) throws
            RecipeNotFoundException,
            FileNotSavedException;

    /**
     * Get recipe by id
     *
     * @param recipeId the id
     * @return {@link RecipeDetailsDto}
     * @throws RecipeNotFoundException when recipe is not found
     */
    RecipeDetailsDto getRecipeById(long recipeId) throws
            RecipeNotFoundException;

    /**
     * Get image by id
     *
     * @param recipeId the id
     * @return The image
     * @throws RecipeNotFoundException when recipe is not found
     * @throws FileNotFoundException when image is not found
     */
    FileSystemResource getRecipeImageById(long recipeId) throws
            RecipeNotFoundException,
            FileNotFoundException;

    /**
     * Get recipe list paginated
     *
     * @param ingredients list of ingredients
     * @param paginated {@link Paginated}
     * @return {@link Paginated<RecipeDetailsDto>}
     */
    Paginated<RecipeDetailsDto> getRecipeList(List<Long> ingredients, PageRequest paginated);

    /**
     * Update Recipe by id
     *
     * @param recipeId the recipe id
     * @param userId the user id
     * @param updateRecipe {@link UpdateRecipeDto}
     * @return {@link RecipeDetailsDto}
     * @throws DatabaseCommunicationException when database communication is not established
     * @throws IngredientNotFoundException when ingredient is not found
     * @throws UserNotFoundException when user is not found
     * @throws RecipeNotFoundException when recipe is not found
     */
    RecipeDetailsDto updateRecipeById(long recipeId, long userId, UpdateRecipeDto updateRecipe) throws
            DatabaseCommunicationException,
            IngredientNotFoundException,
            UserNotFoundException,
            RecipeNotFoundException;

    /**
     * Updates image from recipe by id
     *
     * @param multipartImage the image
     * @param recipeId the recipe id
     * @param userId the user id
     * @return {@link RecipeDetailsDto}
     * @throws RecipeNotFoundException when recipe is not found
     * @throws ReadingFileException when file isn't read
     * @throws FileNotAnImageException when file is not an image
     * @throws FileNotFoundException when file is not found
     * @throws FileNotSavedException when file isn't saved
     */
    RecipeDetailsDto updateImageToRecipe(MultipartFile multipartImage, long userId, long recipeId) throws
            UserNotFoundException,
            RecipeNotFoundException,
            ReadingFileException,
            FileNotAnImageException,
            FileNotFoundException,
            FileNotSavedException;

    /**
     * Delete recipe by id
     *
     * @param recipeId the id
     * @throws RecipeNotFoundException when recipe is not found
     * @throws FileNotFoundException when file is not found
     * @throws DatabaseCommunicationException when database communication is not established
     */
    void deleteRecipe(long recipeId) throws
            RecipeNotFoundException,
            FileNotFoundException,
            DatabaseCommunicationException;
}
