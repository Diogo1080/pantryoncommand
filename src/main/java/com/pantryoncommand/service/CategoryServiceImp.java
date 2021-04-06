package com.pantryoncommand.service;

import com.pantryoncommand.command.Paginated;
import com.pantryoncommand.command.category.CategoryDetailsDto;
import com.pantryoncommand.command.category.CreateOrUpdateCategoryDto;
import com.pantryoncommand.converters.CategoryConverter;
import com.pantryoncommand.error.ErrorMessages;
import com.pantryoncommand.exeption.DatabaseCommunicationException;
import com.pantryoncommand.exeption.category.CategoryAlreadyExistsException;
import com.pantryoncommand.exeption.category.CategoryNotFoundException;
import com.pantryoncommand.persistence.entity.CategoryEntity;
import com.pantryoncommand.persistence.repository.CategoryRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * An {@link CategoryService} implementation
 */
@Service
public class CategoryServiceImp implements CategoryService {

    private final CategoryRepository categoryRepository;
    private static final Logger LOGGER = LogManager.getLogger(CategoryServiceImp.class);

    public CategoryServiceImp(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    /**
     * @see CategoryService#addNewCategory(CreateOrUpdateCategoryDto)
     */
    @Override
    public CategoryDetailsDto addNewCategory(CreateOrUpdateCategoryDto categoryDetails)
            throws CategoryAlreadyExistsException, DatabaseCommunicationException {

        //Build categoryEntity
        CategoryEntity categoryEntity = CategoryConverter.fromCreateOrUpdateCategoryDtoToCategoryEntity(categoryDetails);

        // Persist category into database
        try {
            LOGGER.debug("Saving category into database");
            categoryRepository.save(categoryEntity);

        }catch(DataIntegrityViolationException sqlException){
            LOGGER.error(ErrorMessages.CATEGORY_ALREADY_EXISTS);
            throw new CategoryAlreadyExistsException(ErrorMessages.CATEGORY_ALREADY_EXISTS);

        } catch (Exception e){
            LOGGER.error("Failed while saving category into database - {} - with exception - {}", categoryEntity, e);
            throw new DatabaseCommunicationException(ErrorMessages.DATABASE_COMMUNICATION_ERROR, e);

        }

        // Build CategoryDetailsDto to return to the client
        return CategoryConverter.fromCategoryEntityToCategoryDetailsDto(categoryEntity);
    }

    /**
     * @see CategoryService#getCategoryById(long)
     */
    @Override
    public CategoryDetailsDto getCategoryById(long categoryId) throws CategoryNotFoundException {

        // Get category details from database
        LOGGER.debug("Getting Category with id {}", categoryId);

        CategoryEntity categoryEntity = categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    LOGGER.error(ErrorMessages.CATEGORY_NOT_FOUND);
                    return new CategoryNotFoundException(ErrorMessages.CATEGORY_NOT_FOUND);
                });

        // Build CategoryDetailsDto to return to the client
        return CategoryConverter.fromCategoryEntityToCategoryDetailsDto(categoryEntity);
    }

    /**
     * @see CategoryService#getCategoryList(Pageable)
     */
    @Override
    public Paginated<CategoryDetailsDto> getCategoryList(Pageable pagination) {

        // Get all categories from database with pagination
        LOGGER.debug("Getting all category with pagination - {}", pagination);
        Page<CategoryEntity> categoryList = categoryRepository.findAll(pagination);

        // Convert list items from CategoryEntity to CategoryDetailsDto
        List<CategoryDetailsDto> categoryListResponse = new ArrayList<>();
        for (CategoryEntity CategoryEntity : categoryList.getContent()) {
            categoryListResponse.add(CategoryConverter.fromCategoryEntityToCategoryDetailsDto(CategoryEntity));
        }

        //Build paginated
        Paginated<CategoryDetailsDto> paginated = new Paginated<>(
                categoryListResponse,
                categoryListResponse.size(),
                pagination.getPageNumber(),
                categoryList.getTotalPages(),
                categoryList.getTotalElements()
        );

        //Return paginated
        return paginated;
    }

    /**
     * @see CategoryService#deleteCategory(long)
     */
    @Override
    public void deleteCategory(long categoryId) throws CategoryNotFoundException, DatabaseCommunicationException {
        // Verify if the category exists
        LOGGER.debug("Getting category with id {}", categoryId);
        CategoryEntity categoryEntity = categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    LOGGER.error(ErrorMessages.CATEGORY_NOT_FOUND);
                    return new CategoryNotFoundException(ErrorMessages.CATEGORY_NOT_FOUND);
                });

        // Delete category
        try {
            LOGGER.debug("Deleting category from database");
            categoryRepository.delete(categoryEntity);

        } catch (Exception e) {
            LOGGER.error("Failed while deleting category from database - {} - with exception - {}", categoryEntity, e);
            throw new DatabaseCommunicationException(ErrorMessages.DATABASE_COMMUNICATION_ERROR, e);

        }
    }

    /**
     * @see CategoryService#updateCategory(long, CreateOrUpdateCategoryDto)
     */
    @Override
    public CategoryDetailsDto updateCategory(long categoryId, CreateOrUpdateCategoryDto updateCategory) throws CategoryNotFoundException, DatabaseCommunicationException {

        // Verify if the category exists
        LOGGER.debug("Getting category with id {}", categoryId);
        CategoryEntity categoryEntity = categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    LOGGER.error(ErrorMessages.USER_NOT_FOUND);
                    return new CategoryNotFoundException(ErrorMessages.USER_NOT_FOUND);
                });

        // Update data with categoryDetails received
        categoryEntity.setName(updateCategory.getCategoryName());

        // Save changes
        try {
            LOGGER.debug("Saving updated category into database");
            categoryRepository.save(categoryEntity);

        } catch (Exception e) {
            LOGGER.error("Failed while updating category into database - {} - with exception - {}", categoryEntity, e);
            throw new DatabaseCommunicationException(ErrorMessages.DATABASE_COMMUNICATION_ERROR, e);

        }

        // Convert to CategoryDetailsDto and return updated category
        return CategoryConverter.fromCategoryEntityToCategoryDetailsDto(categoryEntity);
    }
}
