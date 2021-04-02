package com.pantryoncommand.persistence.repository;

import com.pantryoncommand.persistence.entity.HasEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * The has repository for database persistence
 */
public interface HasRepository extends CrudRepository<HasEntity, Long> {
}
