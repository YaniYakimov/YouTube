package com.youtube.youtube.model.repositories;

import com.youtube.youtube.model.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Integer> {
    Optional<Location> findByCountry(String country);
}
