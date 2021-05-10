package com.pantryoncommand.controller;



import com.pantryoncommand.command.Paginated;
import com.pantryoncommand.command.ingredient.CreateOrUpdateIngredientDto;
import com.pantryoncommand.command.ingredient.IngredientDetailsDto;
import com.pantryoncommand.service.IngredientServiceImp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.OK;

/**
 * Ingredient Controller provides end points for CRUD operations on categories
 */
@RestController
@RequestMapping("/api/ingredients")
public class IngredientController {
    private static final Logger LOGGER = LogManager.getLogger(IngredientController.class);
    private final IngredientServiceImp ingredientService;

    public IngredientController(IngredientServiceImp ingredientService){
        this.ingredientService = ingredientService;
    }

    /**
     * Create new ingredient
     * @param createOrUpdateIngredientDto {@link CreateOrUpdateIngredientDto}
     * @return {@link IngredientDetailsDto}
     */
    @PostMapping
    @PreAuthorize("@authorized.hasRole(\"ADMIN\") || " +
            "@authorized.hasRole(\" MOD \")"
    )
    public ResponseEntity<IngredientDetailsDto> createIngredient(
            @Valid @RequestBody CreateOrUpdateIngredientDto createOrUpdateIngredientDto){

        LOGGER.info("Request to create Ingredient - {}", createOrUpdateIngredientDto);
        IngredientDetailsDto ingredientDetails = ingredientService.addNewIngredient(createOrUpdateIngredientDto);

        LOGGER.info("Retrieving created Ingredient - {}", ingredientDetails);
        return new ResponseEntity<>(ingredientDetails, HttpStatus.CREATED);
    }

    /**
     * Get ingredient with certain id
     * @param ingredientId Receives ingredient id
     * @return {@link IngredientDetailsDto}
     */
    @GetMapping("/{ingredientId}")
    @PreAuthorize("@authorized.hasRole(\"ADMIN\") || " +
            "@authorized.hasRole(\" MOD \")"
    )
    public ResponseEntity<IngredientDetailsDto> getIngredientById(@PathVariable long ingredientId) {
        LOGGER.info("Request to get ingredient of id - {}", ingredientId);
        IngredientDetailsDto ingredientDetailsDto = ingredientService.getIngredientById(ingredientId);

        LOGGER.info("Retrieving ingredient with info - {}",ingredientDetailsDto);
        return new ResponseEntity<>(ingredientDetailsDto, OK);
    }

    /**
     * Gets list of categories from database with pagination
     * @param page the page number
     * @param size the number of elements per page
     * @return {@link Paginated<IngredientDetailsDto>}
     */
    @GetMapping
    @PreAuthorize("@authorized.hasRole(\"ADMIN\") || " +
            "@authorized.hasRole(\" MOD \") ||" +
            "@authorized.hasRole(\" USER \")"
    )
    public ResponseEntity<Paginated<IngredientDetailsDto>> getIngredientList(
            @RequestParam(defaultValue="0") long categoryId,
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="10") int size) {
        LOGGER.info("Request to get ingredient list with page and size - {} {}", page, size);
        Paginated<IngredientDetailsDto> ingredientList = ingredientService.getIngredientList(categoryId, PageRequest.of(page, size));

        LOGGER.info("Retrieving List of ingredient with info - {}", ingredientList);
        return new ResponseEntity<>(ingredientList, OK);
    }

    /**
     * Update ingredient with certain id
     * @param ingredientId Receives ingredient id
     * @param createOrUpdateIngredientDto {@link CreateOrUpdateIngredientDto}
     * @return {@link IngredientDetailsDto}
     */
    @PutMapping("/{ingredientId}")
    @PreAuthorize("@authorized.hasRole(\"ADMIN\") || " +
            "@authorized.hasRole(\" MOD \")"
    )
    public ResponseEntity<IngredientDetailsDto> updateIngredient(
            @PathVariable long ingredientId,
            @Valid @RequestBody CreateOrUpdateIngredientDto createOrUpdateIngredientDto) {
        LOGGER.info("Request to update ingredient of id - {} - with info - {}", ingredientId,createOrUpdateIngredientDto);
        IngredientDetailsDto ingredientDetailsDto = ingredientService.updateIngredient(ingredientId, createOrUpdateIngredientDto);

        LOGGER.info("Retrieving updated of ingredient with info - {}",ingredientDetailsDto);
        return new ResponseEntity<>(ingredientDetailsDto, OK);
    }

    /**
     * Delete ingredient with certain id
     * @param ingredientId Receives ingredient id
     * @return Ok if deleted
     */
    @DeleteMapping("/{ingredientId}")
    @PreAuthorize("@authorized.hasRole(\"ADMIN\") || " +
            "@authorized.hasRole(\" MOD \")"
    )
    public ResponseEntity deleteIngredient(@PathVariable long ingredientId) {
        LOGGER.info("Request to delete ingredient of id - {}", ingredientId);
        ingredientService.deleteIngredient(ingredientId);

        LOGGER.info("Responding on successful delete");
        return new ResponseEntity(OK);
    }
}
