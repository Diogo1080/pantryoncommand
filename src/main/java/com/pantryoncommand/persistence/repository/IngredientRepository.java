package com.pantryoncommand.persistence.repository;

import com.pantryoncommand.persistence.entity.IngredientEntity;
import com.pantryoncommand.persistence.entity.RecipeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IngredientRepository extends PagingAndSortingRepository<IngredientEntity, Long>{

    @Query(value = "SELECT * FROM ingredients \n" +
            " WHERE ingredients.category_id=:categoryId\n"
            ,nativeQuery = true)
    Page<IngredientEntity> findAllByCategoryId(@Param("categoryId") long categoryId, Pageable pagination);
}
