package com.ot.weather.repository;

import com.ot.weather.entity.Place;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlacesDataRepository extends CrudRepository<Place, Long> {
}
