package com.pantryoncommand.persistence.repository;

import com.pantryoncommand.persistence.entity.RecipeEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends PagingAndSortingRepository<RecipeEntity, Long> {

}
