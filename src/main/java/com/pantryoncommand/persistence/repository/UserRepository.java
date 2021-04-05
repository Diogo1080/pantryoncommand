package com.pantryoncommand.persistence.repository;

import com.pantryoncommand.persistence.entity.UserEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {
}
