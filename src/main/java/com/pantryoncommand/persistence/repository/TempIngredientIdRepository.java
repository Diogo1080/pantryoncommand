package com.pantryoncommand.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TempIngredientIdRepository extends CrudRepository<com.pantryoncommand.persistence.entity.TempIngredientIdEntity, Long> { }
