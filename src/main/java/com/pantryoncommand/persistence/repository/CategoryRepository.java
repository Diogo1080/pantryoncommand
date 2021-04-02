package com.pantryoncommand.persistence.repository;

import com.pantryoncommand.persistence.entity.CategoryEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * The Category Repository for database persistence
 */
public interface CategoryRepository extends CrudRepository<CategoryEntity, Long> {
}
