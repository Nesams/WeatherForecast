package com.ot.weather.controller;


import com.ot.weather.entity.GeneralForecast;
import com.ot.weather.service.WeatherForecastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class WeatherForecastController {

    private final WeatherForecastService weatherForecastService;

    @Autowired
    public WeatherForecastController(WeatherForecastService weatherForecastService) {
        this.weatherForecastService = weatherForecastService;
    }
    @RequestMapping(value = "/weather-forecast", method = RequestMethod.GET)
    public List<GeneralForecast> index() {
        return weatherForecastService.getAllWeatherData();
    }
}
