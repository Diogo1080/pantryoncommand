package com.pantryoncommand.service;

import com.pantryoncommand.command.Paginated;
import com.pantryoncommand.command.category.CreateOrUpdateCategoryDto;
import com.pantryoncommand.command.category.CategoryDetailsDto;
import com.pantryoncommand.exeption.DatabaseCommunicationException;
import com.pantryoncommand.exeption.category.CategoryAlreadyExistsException;
import com.pantryoncommand.exeption.category.CategoryNotFoundException;
import org.springframework.data.domain.Pageable;

/**
 * Common interface for Category service, provides methods to manage categories
 */
public interface CategoryService {
    /**
     * Create new category
     * @param categoryDetails {@link CategoryDetailsDto}
     * @return {@link CategoryDetailsDto}
     * @throws CategoryAlreadyExistsException when the category already exists
     * @throws DatabaseCommunicationException when database connection isn't established
     */
    CategoryDetailsDto addNewCategory(CreateOrUpdateCategoryDto categoryDetails) throws CategoryAlreadyExistsException, DatabaseCommunicationException;

    /**
     * Get given category with id
     * @param categoryId Receives category id
     * @return {@link CategoryDetailsDto}
     * @throws CategoryNotFoundException when category is not found
     */
    CategoryDetailsDto getCategoryById(long categoryId) throws CategoryNotFoundException;


    /**
     * Gets list of categories from database with pagination
     * @param pagination the page and number of elements per page
     * @return {@link Paginated <CategoryDetailsDto>}
     */
    Paginated<CategoryDetailsDto> getCategoryList(Pageable pagination);

    /**
     * Deletes certain Category with id
     * @param categoryId Receives category id
     * @throws CategoryNotFoundException when category is not found
     * @throws DatabaseCommunicationException when database connection isn't established
     */
    void deleteCategory(long categoryId) throws CategoryNotFoundException, DatabaseCommunicationException;

    /**
     * Update category
     * @param categoryId Receives category is
     * @param updateCategory Receives category info
     * @return {@link CategoryDetailsDto}
     * @throws CategoryNotFoundException when category is not found
     * @throws DatabaseCommunicationException when database connection isn't established
     */
    CategoryDetailsDto updateCategory(long categoryId, CreateOrUpdateCategoryDto updateCategory) throws CategoryNotFoundException,DatabaseCommunicationException;
}
