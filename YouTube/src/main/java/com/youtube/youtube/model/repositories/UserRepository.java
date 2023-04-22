package com.youtube.youtube.model.repositories;
import com.youtube.youtube.model.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);

    Optional<User> getUserByEmail(String email);
    @Query("SELECT u FROM users u where u.firstName LIKE %:firstName%")
    Page<User> getUserByFirstName(String firstName, Pageable pageable);

    Optional<User> findAllByConfirmationToken(String token);

}
