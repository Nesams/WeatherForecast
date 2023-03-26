package com.ot.weather;

import com.ot.weather.entity.GeneralForecast;
import com.ot.weather.entity.Place;
import com.ot.weather.entity.Wind;
import com.ot.weather.repository.PlacesDataRepository;
import com.ot.weather.repository.WeatherDataRepository;
import com.ot.weather.repository.WindDataRepository;
import com.ot.weather.service.TemperatureAverageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class WeatherApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private PlacesDataRepository placesDataRepository;

	@Autowired
	private TemperatureAverageService temperatureAverageService;
	@Autowired
	private WeatherDataRepository weatherDataRepository;

	@Autowired
	private WindDataRepository windDataRepository;

	@Test
	public void testGetAverageWithCurrentData() throws IOException {
		temperatureAverageService.getAverageTemperature();

		double expectedMinTemp = 2.5;
		double expectedMaxTemp = 4.0;

		File file = new File("src/main/java/com/ot/weather/stats/temperature_stats.txt");
		BufferedReader reader = new BufferedReader(new FileReader(file));
		assertEquals("Average minimum temperature: " + expectedMinTemp, reader.readLine());
		assertEquals("Average maximum temperature: " + expectedMaxTemp, reader.readLine());
		reader.close();

	}

	@Test
	public void testAlwaysHaveFourGeneralForecasts() {
		List<GeneralForecast> places = (List<GeneralForecast>) weatherDataRepository.findAll();
		assertEquals(4, places.size());
	}

	@Test
	public void testAlwaysHaveTwelvePlaces() {
		List<Place> places = (List<Place>) placesDataRepository.findAll();
		assertEquals(12, places.size());
	}

	@Test
	public void testAlwaysHaveSixPlacesForNight() {
		List<Place> places = placesDataRepository.findByNightOrDay("night");
		assertEquals(6, places.size());
	}

	@Test
	public void testAlwaysHaveSixWindSpeeds() {
		List<Wind> windSpeeds = (List<Wind>) windDataRepository.findAll();
		assertEquals(6, windSpeeds.size());
	}
}

