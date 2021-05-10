package com.pantryoncommand.persistence.repository;

import com.pantryoncommand.command.Paginated;
import com.pantryoncommand.persistence.entity.RecipeEntity;
import com.pantryoncommand.persistence.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends PagingAndSortingRepository<RecipeEntity, Long> {
    @Query(value = "Select A.* from recipes AS A INNER JOIN recipe_has AS B ON A.recipe_id=B.recipe_id INNER JOIN temp_ingredients AS C ON B.ingredient_id=C.entity_id GROUP BY A.recipe_id"
            ,nativeQuery = true)
    Page<RecipeEntity> findAllByIngredientId(Pageable pagination);
}
