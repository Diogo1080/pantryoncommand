package com.pantryoncommand.persistence.repository;

import com.pantryoncommand.persistence.entity.RecipeEntity;
import org.springframework.data.repository.CrudRepository;

public interface RecipeRepository extends CrudRepository<RecipeEntity, Long> {
}
