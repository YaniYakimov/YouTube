package com.youtube.youtube.model.repositories;

import com.youtube.youtube.model.entities.Video;
import com.youtube.youtube.model.entities.Visibility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VisibilityRepository extends JpaRepository<Visibility, Integer> {

}
