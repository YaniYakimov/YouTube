package com.youtube.youtube.model.repositories;
import com.youtube.youtube.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);

    Optional<User> getUserByEmail(String email);
    List<User> getUserByFirstName(String firstName);

    Optional<User> findAllByConfirmationToken(String token);

}
