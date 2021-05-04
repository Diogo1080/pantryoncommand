package com.pantryoncommand.persistence.repository;

import com.pantryoncommand.persistence.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {

    /**
     * Get user by email
     * @param email
     * @return
     */
    Optional<UserEntity> findByEmail(String email);

    @Query(value = "SELECT * FROM users WHERE role = :role",
            countQuery = "SELECT * FROM users WHERE role = :role",
            nativeQuery = true)
    Page<UserEntity> findAllByRole(@Param("role") String role, Pageable pageable);
}
