package com.youtube.youtube.model.repositories;
import com.youtube.youtube.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
