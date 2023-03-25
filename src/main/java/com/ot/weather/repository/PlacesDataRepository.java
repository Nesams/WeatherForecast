package com.ot.weather.repository;

import com.ot.weather.entity.Place;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlacesDataRepository extends CrudRepository<Place, Long> {
    @Query("SELECT p FROM Place p WHERE p.nightOrDay = ?1")
    List<Place> findByNightOrDay(String nightOrDay);
}
