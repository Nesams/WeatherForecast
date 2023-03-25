package com.ot.weather.service;

import com.ot.weather.entity.GeneralForecast;
import com.ot.weather.entity.Place;
import com.ot.weather.entity.Wind;
import com.ot.weather.repository.PlacesDataRepository;
import com.ot.weather.repository.WeatherDataRepository;
import com.ot.weather.repository.WindDataRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Component
public class WeatherDataImportService {

    @Autowired
    private Environment env;

    @Autowired
    private WeatherDataRepository weatherDataRepository;
    @Autowired
    private PlacesDataRepository placesDataRepository;
    @Autowired
    private WindDataRepository windDataRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherDataImportService.class);

    @PostConstruct
    @Scheduled(cron = "1 15 * * * *")
    public void importData() {
        windDataRepository.deleteAll();
        placesDataRepository.deleteAll();
        weatherDataRepository.deleteAll();

        try {
            URL url = new URL("https://www.ilmateenistus.ee/ilma_andmed/xml/forecast.php?lang=eng");
            InputStream stream = url.openStream();
            Document doc = createDocument(stream);

            NodeList forecastList = doc.getElementsByTagName("forecast");
            for (int i = 0; i < forecastList.getLength(); i++) {
                Node forecastNode = forecastList.item(i);

                if (forecastNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element forecast = (Element) forecastList.item(i);
                    GeneralForecast generalForecast = extractGeneralForecast(forecast);

                    processTimePeriods(forecast, generalForecast, "night");
                    processTimePeriods(forecast, generalForecast, "day");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        LOGGER.info("Importing weather data");
    }

    private Document createDocument(InputStream stream) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(stream);
        doc.getDocumentElement().normalize();
        return doc;
    }

    private GeneralForecast extractGeneralForecast(Element forecast) {
        GeneralForecast generalForecast = new GeneralForecast();
        generalForecast.setDate(forecast.getAttribute("date"));
        return generalForecast;
    }

    private void processTimePeriods(Element forecast, GeneralForecast generalForecast, String timePeriod) {
        NodeList timePeriodNodes = forecast.getElementsByTagName(timePeriod);
        for (int j = 0; j < timePeriodNodes.getLength(); j++) {
            Node timePeriodNode = timePeriodNodes.item(j);
            if (timePeriodNode.getNodeType() == Node.ELEMENT_NODE) {
                Element timePeriodElement = (Element) timePeriodNode;
                setGeneralForecastProperties(generalForecast, timePeriodElement, timePeriod);
                weatherDataRepository.save(generalForecast);

                List<Place> placesToSave = extractPlaces(timePeriodElement, generalForecast, timePeriod);
                placesDataRepository.saveAll(placesToSave);

                List<Wind> windsToSave = extractWinds(timePeriodElement, generalForecast, timePeriod);
                windDataRepository.saveAll(windsToSave);
            }
        }
    }

    private void setGeneralForecastProperties(GeneralForecast generalForecast, Element timePeriodElement, String timePeriod) {
        generalForecast.setPhenomenon(timePeriod, timePeriodElement.getElementsByTagName("phenomenon").item(0).getTextContent());
        generalForecast.setTempMin(timePeriod, Integer.parseInt(timePeriodElement.getElementsByTagName("tempmin").item(0).getTextContent()));
        generalForecast.setTempMax(timePeriod, Integer.parseInt(timePeriodElement.getElementsByTagName("tempmax").item(0).getTextContent()));
        generalForecast.setText(timePeriod, timePeriodElement.getElementsByTagName("text").item(0).getTextContent());

        if (timePeriodElement.getElementsByTagName("sea").item(0) != null && timePeriodElement.getElementsByTagName("peipsi").item(0) != null) {
            if (timePeriod.equals("night")) {
                generalForecast.setSeaNightOverView(timePeriodElement.getElementsByTagName("sea").item(0).getTextContent());
                generalForecast.setPeipsiNight(timePeriodElement.getElementsByTagName("peipsi").item(0).getTextContent());
            }
            if (timePeriod.equals("day")) {
                generalForecast.setSeaDayOverView(timePeriodElement.getElementsByTagName("sea").item(0).getTextContent());
                generalForecast.setPeipsiDay(timePeriodElement.getElementsByTagName("peipsi").item(0).getTextContent());
            }
        }
        }

    private List<Place> extractPlaces(Element timePeriodElement, GeneralForecast generalForecast, String timePeriod) {
        List<Place> placesToSave = new ArrayList<>();
        NodeList placeList = timePeriodElement.getElementsByTagName("place");
        for (int k = 0; k < placeList.getLength(); k++) {
            Node placeNode = placeList.item(k);
            if (placeNode.getNodeType() == Node.ELEMENT_NODE) {
                Element place = (Element) placeList.item(k);
                Place placeForecast = createPlaceForecast(place, generalForecast, timePeriod);
                placesToSave.add(placeForecast);
            }
        }
        return placesToSave;
    }

    private Place createPlaceForecast(Element place, GeneralForecast generalForecast, String timePeriod) {
        Place placeForecast = new Place();
        placeForecast.setName(place.getElementsByTagName("name").item(0).getTextContent());
        placeForecast.setPhenomenon(place.getElementsByTagName("phenomenon").item(0).getTextContent());
        if (timePeriod.equals("night")) {
            placeForecast.setTempMin(Integer.parseInt(place.getElementsByTagName("tempmin").item(0).getTextContent()));
        }
        if (timePeriod.equals("day")) {
            placeForecast.setTempMax(Integer.parseInt(place.getElementsByTagName("tempmax").item(0).getTextContent()));
        }
        placeForecast.setNightOrDay(timePeriod);
        placeForecast.setGeneralForecast(generalForecast);
        return placeForecast;
    }

    private List<Wind> extractWinds(Element timePeriodElement, GeneralForecast generalForecast, String timePeriod) {
        List<Wind> windsToSave = new ArrayList<>();
        NodeList windList = timePeriodElement.getElementsByTagName("wind");
        for (int l = 0; l < windList.getLength(); l++) {
            Element wind = (Element) windList.item(l);
            Wind windForecast = createWindForecast(wind, generalForecast, timePeriod);
            windsToSave.add(windForecast);
        }
        return windsToSave;
    }

    private Wind createWindForecast(Element wind, GeneralForecast generalForecast, String timePeriod) {
        Wind windForecast = new Wind();
        windForecast.setDirection(wind.getElementsByTagName("direction").item(0).getTextContent());
        windForecast.setName(wind.getElementsByTagName("name").item(0).getTextContent());
        windForecast.setSpeedMin(Integer.parseInt(wind.getElementsByTagName("speedmin").item(0).getTextContent()));
        windForecast.setSpeedMax(Integer.parseInt(wind.getElementsByTagName("speedmax").item(0).getTextContent()));
        windForecast.setGust(wind.getElementsByTagName("gust").item(0).getTextContent());
        windForecast.setNightOrDay(timePeriod);
        windForecast.setGeneralForecast(generalForecast);
        return windForecast;
    }
}
