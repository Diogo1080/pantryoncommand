package com.pantryoncommand.controller;



import com.pantryoncommand.command.Paginated;
import com.pantryoncommand.command.category.CategoryDetailsDto;
import com.pantryoncommand.command.category.CreateOrUpdateCategoryDto;
import com.pantryoncommand.service.CategoryServiceImp;
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
 * Category Controller provides end points for CRUD operations on categories
 */
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private static final Logger LOGGER = LogManager.getLogger(CategoryController.class);
    private final CategoryServiceImp categoryService;

    public CategoryController(CategoryServiceImp categoryService){
        this.categoryService = categoryService;
    }

    /**
     * Create new category
     * @param createOrUpdateCategoryDto {@link CreateOrUpdateCategoryDto}
     * @return {@link CategoryDetailsDto}
     */
    @PostMapping
    @PreAuthorize("@authorized.hasRole(\"ADMIN\") || " +
            "@authorized.hasRole(\" MOD \")"
    )
    public ResponseEntity<CategoryDetailsDto> createCategory(
            @Valid @RequestBody CreateOrUpdateCategoryDto createOrUpdateCategoryDto){

        LOGGER.info("Request to create Category - {}", createOrUpdateCategoryDto);
        CategoryDetailsDto categoryDetails = categoryService.addNewCategory(createOrUpdateCategoryDto);

        LOGGER.info("Retrieving created Category - {}", categoryDetails);
        return new ResponseEntity<>(categoryDetails, HttpStatus.CREATED);
    }

    /**
     * Get category with certain id
     * @param categoryId Receives category id
     * @return {@link CategoryDetailsDto}
     */
    @GetMapping("/{categoryId}")
    @PreAuthorize("@authorized.hasRole(\"ADMIN\") || " +
            "@authorized.hasRole(\" MOD \")"
    )
    public ResponseEntity<CategoryDetailsDto> getCategoryById(@PathVariable long categoryId) {
        LOGGER.info("Request to get category of id - {}", categoryId);
        CategoryDetailsDto categoryDetailsDto = categoryService.getCategoryById(categoryId);

        LOGGER.info("Retrieving category with info - {}",categoryDetailsDto);
        return new ResponseEntity<>(categoryDetailsDto, OK);
    }

    /**
     * Gets list of categories from database with pagination
     * @param page the page number
     * @param size the number of elements per page
     * @return {@link Paginated<CategoryDetailsDto>}
     */
    @GetMapping
    @PreAuthorize("@authorized.hasRole(\"ADMIN\") || " +
            "@authorized.hasRole(\" MOD \") ||" +
            "@authorized.hasRole(\"USER\")"
    )
    public ResponseEntity<Paginated<CategoryDetailsDto>> getCategoryList(@RequestParam(defaultValue="0") int page,
                                                                          @RequestParam(defaultValue="10") int size) {
        LOGGER.info("Request to get category list with page and size - {} {}", page, size);
        Paginated<CategoryDetailsDto> categoryList = categoryService.getCategoryList(PageRequest.of(page, size));

        LOGGER.info("Retrieving List of category with info - {}", categoryList);
        return new ResponseEntity<>(categoryList, OK);
    }

    /**
     * Update category with certain id
     * @param categoryId Receives category id
     * @param createOrUpdateCategoryDto {@link CreateOrUpdateCategoryDto}
     * @return {@link CategoryDetailsDto}
     */
    @PutMapping("/{categoryId}")
    @PreAuthorize("@authorized.hasRole(\"ADMIN\") || " +
            "@authorized.hasRole(\" MOD \")"
    )
    public ResponseEntity<CategoryDetailsDto> updateCategory(@PathVariable long categoryId,
                                                     @Valid @RequestBody CreateOrUpdateCategoryDto createOrUpdateCategoryDto) {
        LOGGER.info("Request to update category of id - {} - with info - {}", categoryId,createOrUpdateCategoryDto);
        CategoryDetailsDto categoryDetailsDto = categoryService.updateCategory(categoryId, createOrUpdateCategoryDto);

        LOGGER.info("Retrieving updated of category with info - {}",categoryDetailsDto);
        return new ResponseEntity<>(categoryDetailsDto, OK);
    }

    /**
     * Delete category with certain id
     * @param categoryId Receives category id
     * @return Ok if deleted
     */
    @DeleteMapping("/{categoryId}")
    @PreAuthorize("@authorized.hasRole(\"ADMIN\") || " +
            "@authorized.hasRole(\" MOD \")"
    )
    public ResponseEntity deleteCategory(@PathVariable long categoryId) {
        LOGGER.info("Request to delete category of id - {}", categoryId);
        categoryService.deleteCategory(categoryId);

        LOGGER.info("Responding on successful delete");
        return new ResponseEntity(OK);
    }
}
