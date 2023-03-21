package com.ot.weather.service;

import com.ot.weather.repository.WeatherDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WeatherForecastService {

    @Autowired
    private WeatherDataRepository weatherDataRepository;
}
