package com.pantryoncommand.persistence.repository;

import com.pantryoncommand.persistence.entity.CategoryEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * The Category Repository for database persistence
 */
public interface CategoryRepository extends PagingAndSortingRepository<CategoryEntity, Long> {
}
