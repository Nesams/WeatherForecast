package com.ot.weather.repository;

import com.ot.weather.entity.Wind;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WindDataRepository extends CrudRepository<Wind, Long> {
}
