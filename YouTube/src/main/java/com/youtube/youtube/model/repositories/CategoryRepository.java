package com.youtube.youtube.model.repositories;

import com.youtube.youtube.model.entities.Category;
import com.youtube.youtube.model.entities.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
