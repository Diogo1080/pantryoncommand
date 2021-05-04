package com.pantryoncommand.service;

import com.pantryoncommand.command.Paginated;
import com.pantryoncommand.command.ingredient.CreateOrUpdateIngredientDto;
import com.pantryoncommand.command.ingredient.IngredientDetailsDto;
import com.pantryoncommand.converters.IngredientConverter;
import com.pantryoncommand.error.ErrorMessages;
import com.pantryoncommand.exeption.DatabaseCommunicationException;
import com.pantryoncommand.exeption.category.CategoryNotFoundException;
import com.pantryoncommand.exeption.ingredient.IngredientAlreadyExistsException;
import com.pantryoncommand.exeption.ingredient.IngredientNotFoundException;
import com.pantryoncommand.persistence.entity.CategoryEntity;
import com.pantryoncommand.persistence.entity.IngredientEntity;
import com.pantryoncommand.persistence.repository.CategoryRepository;
import com.pantryoncommand.persistence.repository.IngredientRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * An {@link IngredientService} implementation
 */
@Service
public class IngredientServiceImp implements IngredientService{

    private final IngredientRepository ingredientRepository;
    private final CategoryRepository categoryRepository;
    private static final Logger LOGGER = LogManager.getLogger(IngredientServiceImp.class);

    public IngredientServiceImp(IngredientRepository ingredientRepository, CategoryRepository categoryRepository) {
        this.ingredientRepository = ingredientRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * @see IngredientService#addNewIngredient(CreateOrUpdateIngredientDto)
     */
    public IngredientDetailsDto addNewIngredient(CreateOrUpdateIngredientDto createIngredient)
            throws  CategoryNotFoundException,
                    IngredientAlreadyExistsException,
                    DatabaseCommunicationException {

        // Verify if category exists
        LOGGER.debug("Getting category with id {}", createIngredient.getCategoryId());
        CategoryEntity categoryEntity = categoryRepository.findById(createIngredient.getCategoryId())
                .orElseThrow(() -> {
                    LOGGER.error(ErrorMessages.CATEGORY_NOT_FOUND);
                    return new CategoryNotFoundException(ErrorMessages.CATEGORY_NOT_FOUND);
                });

        // Build IngredientEntity
        IngredientEntity ingredientEntity = IngredientConverter.fromCreateOrUpdateIngredientDtoToIngredientEntity(createIngredient);
        ingredientEntity.setCategoryEntity(categoryEntity);

        // Persist ingredient into database
        try {
            LOGGER.debug("Saving ingredient into database");
            ingredientRepository.save(ingredientEntity);

        } catch (DataIntegrityViolationException sqlException) {
            LOGGER.error(ErrorMessages.INGREDIENT_ALREADY_EXISTS);
            throw new IngredientAlreadyExistsException(ErrorMessages.INGREDIENT_ALREADY_EXISTS);

        } catch (Exception e) {
            LOGGER.error("Failed while saving ingredient into database - {} - with exception - {}", ingredientEntity, e);
            throw new DatabaseCommunicationException(ErrorMessages.DATABASE_COMMUNICATION_ERROR, e);

        }

        // Build IngredientDetailsDto to return to the client
        return IngredientConverter.fromIngredientEntityToIngredientDetailsDto(ingredientEntity);
    }

    /**
     * @see IngredientService#getIngredientById(long)
     */
    public IngredientDetailsDto getIngredientById(long ingredientId) throws IngredientNotFoundException {

        // Get ingredient details from database
        LOGGER.debug("Getting ingredient with id {}", ingredientId);
        IngredientEntity ingredientEntity = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> {
                    LOGGER.error(ErrorMessages.INGREDIENT_NOT_FOUND);
                    return new IngredientNotFoundException(ErrorMessages.INGREDIENT_NOT_FOUND);
                });

        // Build IngredientDetailsDto to return to the client
        return IngredientConverter.fromIngredientEntityToIngredientDetailsDto(ingredientEntity);
    }

    /**
     * @see IngredientService#getIngredientList(Pageable)
     */
    public Paginated<IngredientDetailsDto> getIngredientList(Pageable pagination) {

        // Get all ingredients from database with pagination
        LOGGER.debug("Getting all ingredients with pagination - {}", pagination);
        Page<IngredientEntity> ingredientsList = ingredientRepository.findAll(pagination);

        // Convert list items from IngredientEntity to IngredientDetailsDto
        List<IngredientDetailsDto> ingredientsListResponse = new ArrayList<>();
        for (IngredientEntity ingredientEntity : ingredientsList.getContent()) {
            ingredientsListResponse.add(IngredientConverter.fromIngredientEntityToIngredientDetailsDto(ingredientEntity));
        }

        //Build paginated
        Paginated<IngredientDetailsDto> paginated = new Paginated<>(
                ingredientsListResponse,
                ingredientsListResponse.size(),
                pagination.getPageNumber(),
                ingredientsList.getTotalPages(),
                ingredientsList.getTotalElements()
        );

        //Return paginated
        return paginated;
    }

    /**
     * @see IngredientService#deleteIngredient(long)
     */
    public void deleteIngredient(long ingredientId) throws IngredientNotFoundException, DatabaseCommunicationException {

        // Verify if the ingredient exists
        LOGGER.debug("Getting ingredient with id {}", ingredientId);
        IngredientEntity ingredientEntity = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> {
                    LOGGER.error(ErrorMessages.INGREDIENT_NOT_FOUND);
                    return new IngredientNotFoundException(ErrorMessages.INGREDIENT_NOT_FOUND);
                });

        // Delete ingredient
        try {
            LOGGER.debug("Deleting ingredient from database");
            ingredientRepository.delete(ingredientEntity);

        } catch (Exception e) {
            LOGGER.error("Failed while deleting ingredient from database - {} - with exception - {}", ingredientEntity, e);
            throw new DatabaseCommunicationException(ErrorMessages.DATABASE_COMMUNICATION_ERROR, e);
        }
    }

    /**
     * @see IngredientService#updateIngredient(long, CreateOrUpdateIngredientDto)
     */
    public IngredientDetailsDto updateIngredient(long ingredientId, CreateOrUpdateIngredientDto updateIngredient)
            throws  CategoryNotFoundException,
                    IngredientNotFoundException,
                    DatabaseCommunicationException {

        // Verify if the ingredient exists
        LOGGER.debug("Getting ingredient with id {}", ingredientId);
        IngredientEntity ingredientEntity = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> {
                    LOGGER.error(ErrorMessages.INGREDIENT_NOT_FOUND);
                    return new IngredientNotFoundException(ErrorMessages.INGREDIENT_NOT_FOUND);
                });

        // Verify if the category exists
        LOGGER.debug("Getting category with id {}", updateIngredient.getCategoryId());
        CategoryEntity categoryEntity = categoryRepository.findById(updateIngredient.getCategoryId())
                .orElseThrow(() -> {
                    LOGGER.error(ErrorMessages.CATEGORY_NOT_FOUND);
                    return new CategoryNotFoundException(ErrorMessages.CATEGORY_NOT_FOUND);
                });

        // Update data with ingredientDetails received
        ingredientEntity.setName(updateIngredient.getName());
        ingredientEntity.setCategoryEntity(categoryEntity);

        // Save changes
        try {
            LOGGER.debug("Saving updated ingredient into database");
            ingredientRepository.save(ingredientEntity);

        } catch (Exception e) {
            LOGGER.error("Failed while updating ingredient into database - {} - with exception - {}", ingredientEntity, e);
            throw new DatabaseCommunicationException(ErrorMessages.DATABASE_COMMUNICATION_ERROR, e);

        }

        // Convert to IngredientDetailsDto and return updated ingredient
        return IngredientConverter.fromIngredientEntityToIngredientDetailsDto(ingredientEntity);
    }
}
