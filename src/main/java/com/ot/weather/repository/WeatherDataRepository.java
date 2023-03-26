package com.ot.weather.repository;

import com.ot.weather.entity.GeneralForecast;
import org.springframework.data.repository.CrudRepository;

public interface WeatherDataRepository extends CrudRepository<GeneralForecast, Long> {
}