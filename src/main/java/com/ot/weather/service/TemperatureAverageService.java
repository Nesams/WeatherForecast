package com.ot.weather.service;

import com.ot.weather.entity.Place;
import com.ot.weather.repository.PlacesDataRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Component
@DependsOn("weatherDataImportService")
public class TemperatureAverageService {

    @Autowired
    private PlacesDataRepository placesDataRepository;

    @PostConstruct
    @Scheduled(cron = "1 15 * * * *")
    public void getAverageTemperature() {
        List<Place> night = placesDataRepository.findByNightOrDay("night");
        double averageMinTemp = night.stream().mapToDouble(Place::getTempMin).average().orElse(0);
        List<Place> day = placesDataRepository.findByNightOrDay("day");
        double averageMaxTemp = day.stream().mapToDouble(Place::getTempMax).average().orElse(0);
        File file = new File("src/main/java/com/ot/weather/stats/temperature_stats.txt");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("Average minimum temperature: " + averageMinTemp + "\n");
            writer.write("Average maximum temperature: " + averageMaxTemp + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
