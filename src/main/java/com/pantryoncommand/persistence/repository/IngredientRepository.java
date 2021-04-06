package com.pantryoncommand.persistence.repository;

import com.pantryoncommand.persistence.entity.IngredientEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IngredientRepository extends PagingAndSortingRepository<IngredientEntity, Long> {
}
