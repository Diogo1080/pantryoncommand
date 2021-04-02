package com.pantryoncommand.persistence.repository;

import com.pantryoncommand.persistence.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
}
