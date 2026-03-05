package com.solution.authservice.repository;

import com.solution.authservice.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    @Query("""
                    SELECT u FROM User u
                        LEFT JOIN FETCH u.roles
                        LEFT JOIN FETCH u.credential
                        WHERE u.id = :id
            """)
    Optional<User> findByIdWithDetails(@Param("id") UUID id);

    @Query("""
            SELECT u FROM User u
                LEFT JOIN FETCH u.roles
                LEFT JOIN FETCH u.credential
                WHERE u.username = :username
            """)
    Optional<User> findByUsernameWithDetails(@Param("username") String username);

    @Query("""
            SELECT u FROM User u
                LEFT JOIN FETCH u.credential
                LEFT JOIN FETCH u.roles
            """)
    Page<User> findAllWithDetails(Pageable pageable);
}
