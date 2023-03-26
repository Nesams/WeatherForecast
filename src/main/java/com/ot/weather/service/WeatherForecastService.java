package com.ot.weather.service;

import com.ot.weather.entity.GeneralForecast;
import com.ot.weather.repository.WeatherDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeatherForecastService {

    @Autowired
    private WeatherDataRepository weatherDataRepository;

    public List<GeneralForecast> getAllWeatherData() {
        return (List<GeneralForecast>) weatherDataRepository.findAll();
    }
}
