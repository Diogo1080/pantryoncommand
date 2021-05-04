package com.pantryoncommand.controller;

import com.pantryoncommand.command.Paginated;
import com.pantryoncommand.command.recipe.CreateRecipeDto;
import com.pantryoncommand.command.recipe.UpdateRecipeDto;
import com.pantryoncommand.command.recipe.RecipeDetailsDto;
import com.pantryoncommand.service.RecipeServiceImp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;


/**
 * Recipe Controller provides end points for CRUD operations on recipes
 */
@RestController
@RequestMapping("/api/recipes")
public class RecipeController {
    private static final Logger LOGGER = LogManager.getLogger(RecipeController.class);
    private final RecipeServiceImp recipeServiceImp;

    public RecipeController(RecipeServiceImp recipeServiceImp) {
        this.recipeServiceImp = recipeServiceImp;
    }

    /**
     * Create Recipe
     *
     * @param createRecipeDto {@link CreateRecipeDto}
     * @return {@link RecipeDetailsDto}
     */
    @PostMapping
    public ResponseEntity<RecipeDetailsDto> createRecipe(
            @Valid @RequestBody CreateRecipeDto createRecipeDto){

        LOGGER.info("Request to create Recipe - {}", createRecipeDto);
        RecipeDetailsDto recipeDetails = recipeServiceImp.addNewRecipe(createRecipeDto);

        LOGGER.info("Retrieving created Recipe - {}", recipeDetails);
        return new ResponseEntity<>(recipeDetails, HttpStatus.CREATED);
    }

    /**
     * Create recipe image
     *
     * @param multipartImage the image
     * @param recipeId the id
     * @return {@link RecipeDetailsDto}
     */
    @PostMapping("/{recipeId}/image")
    public ResponseEntity<RecipeDetailsDto> createRecipeImage(
            @RequestParam MultipartFile multipartImage,
            @PathVariable long recipeId){

        LOGGER.info("Request to add photo - {} - to recipe with id - {}", multipartImage, recipeId);
        RecipeDetailsDto recipeDetails = recipeServiceImp.addImageToRecipe(multipartImage, recipeId);

        LOGGER.info("Retrieving recipe with info - {}", recipeDetails);
        return new ResponseEntity<>(recipeDetails,HttpStatus.OK);
    }

    /**
     * Get recipe by id
     *
     * @param recipeId the id
     * @return {@link RecipeDetailsDto}
     */
    @GetMapping("/{recipeId}")
    public ResponseEntity<RecipeDetailsDto> getRecipeById(@PathVariable long recipeId){
        LOGGER.info("Request to get Recipe with Id - {}",recipeId );
        RecipeDetailsDto recipeDetails = recipeServiceImp.getRecipeById(recipeId);

        LOGGER.info("Retrieving recipe with info - {}", recipeDetails);
        return new ResponseEntity<>(recipeDetails,HttpStatus.OK);
    }

    /**
     * Gets the image of recipe by id
     *
     * @param recipeId the id
     * @return the image in FileSystemResource format
     */
    @GetMapping(value = "/{recipeId}/image", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<FileSystemResource> getRecipeImageById(@PathVariable long recipeId){
        LOGGER.info("Request to get Recipe image with Id - {}",recipeId );
        FileSystemResource image = recipeServiceImp.getRecipeImageById(recipeId);

        LOGGER.info("Retrieving recipe image with info - {}", image);
        return new ResponseEntity<>(image,HttpStatus.OK);
    }

    /**
     * Get recipe list with pagination
     *
     * @param page the page
     * @param size the size
     * @return {@link Paginated<RecipeDetailsDto>}
     */
    @GetMapping
    public ResponseEntity<Paginated<RecipeDetailsDto>> getRecipeList(
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="10") int size) {
        LOGGER.info("Request to get recipe list with page and size - {} {}", page, size);
        Paginated<RecipeDetailsDto> recipeList = recipeServiceImp.getRecipeList(PageRequest.of(page, size));

        LOGGER.info("Retrieving List of recipe with info - {}", recipeList);
        return new ResponseEntity<>(recipeList, HttpStatus.OK);
    }

    /**
     * Update recipe by id
     *
     * @param recipeId the id
     * @param updateRecipeDto {@link UpdateRecipeDto}
     * @return {@link RecipeDetailsDto}
     */
    @PutMapping("/{recipeId}/user/{userId}")
    @PreAuthorize("@authorized.isUser(#userId)) ||" +
            "@authorized.hasRole(\"ADMIN\") || " +
            "@authorized.hasRole(\" MOD \") || "
    )
    public ResponseEntity<RecipeDetailsDto> updateRecipe(
            @PathVariable long recipeId,
            @PathVariable long userId,
            @Valid @RequestBody UpdateRecipeDto updateRecipeDto){
        LOGGER.info("Request to update Recipe with Id - {} - of user - {}  - with info - {}",
                recipeId,
                userId,
                updateRecipeDto);
        RecipeDetailsDto recipeDetails = recipeServiceImp.updateRecipeById(recipeId, userId, updateRecipeDto);

        LOGGER.info("Retrieving recipe with info - {}", recipeDetails);
        return new ResponseEntity<>(recipeDetails,HttpStatus.OK);
    }

    /**
     * Update Recipe image by id
     *
     * @param multipartImage the image
     * @param recipeId the id
     * @return {@link RecipeDetailsDto}
     */
    @PutMapping(value = "/{recipeId}/image/user/{userId}")
    @PreAuthorize("@authorized.isUser(#userId) ||" +
            "@authorized.hasRole(\"ADMIN\") || " +
            "@authorized.hasRole(\" MOD \") || "
    )
    public ResponseEntity<RecipeDetailsDto> updateRecipeImage(
            @PathVariable long userId,
            @PathVariable long recipeId,
            @RequestParam MultipartFile multipartImage){

        LOGGER.info("Request to update photo - {} - to recipe with id - {} - from user - {}",
                multipartImage,
                recipeId,
                userId);
        RecipeDetailsDto recipeDetails = recipeServiceImp.updateImageToRecipe(multipartImage, userId, recipeId);

        LOGGER.info("Retrieving recipe with info - {}", recipeDetails);
        return new ResponseEntity<>(recipeDetails,HttpStatus.OK);
    }

    /**
     * Delete recipe by id
     *
     * @param recipeId the id
     * @return Ok if delete was successful
     */
    @DeleteMapping("/{recipeId}/user/{userId}")
    @PreAuthorize("@authorized.isUser(#userId) ||" +
            "@authorized.hasRole(\"ADMIN\") || " +
            "@authorized.hasRole(\" MOD \") || "
    )
    public ResponseEntity deleteRecipe(@PathVariable long recipeId, @PathVariable long userId) {
        LOGGER.info("Request to delete recipe of id - {}", recipeId);
        recipeServiceImp.deleteRecipe(recipeId);

        LOGGER.info("Responding on successful delete");
        return new ResponseEntity(HttpStatus.OK);
    }
}
