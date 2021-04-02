package com.pantryoncommand.persistence.repository;

import com.pantryoncommand.persistence.entity.IngredientEntity;
import org.springframework.data.repository.CrudRepository;

public interface IngredientsRepository extends CrudRepository<IngredientEntity, Long> {
}
