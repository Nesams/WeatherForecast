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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.net.URL;

@Component
public class WeatherDataImportService {

    @Autowired
    private WeatherDataRepository weatherDataRepository;
    @Autowired
    private PlacesDataRepository placesDataRepository;
    @Autowired
    private WindDataRepository windDataRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherDataImportService.class);

    @PostConstruct
    @Scheduled(cron = "1 15 * * * *")
    public void ImportData() {
        try {
            URL url = new URL("https://www.ilmateenistus.ee/ilma_andmed/xml/forecast.php?lang=eng");
            InputStream stream = url.openStream();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(stream);
            doc.getDocumentElement().normalize();
            NodeList forecastList = doc.getElementsByTagName("forecast");
            for (int i = 0; i < forecastList.getLength(); i++) {
                Node forecastNode = forecastList.item(i);

                if (forecastNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element forecast = (Element) forecastList.item(i);

                    GeneralForecast generalForecast = new GeneralForecast();
                    generalForecast.setDate(forecast.getAttribute("date"));

                    NodeList nightNodes = forecast.getElementsByTagName("night");

                    for (int j = 0; j < nightNodes.getLength(); j++) {
                        Node nightNode = nightNodes.item(j);
                        if (nightNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element night = (Element) nightNode;
                            generalForecast.setNightPhenomenon(night.getElementsByTagName("phenomenon").item(0).getTextContent());
                            generalForecast.setNightTempMin(Integer.parseInt(night.getElementsByTagName("tempmin").item(0).getTextContent()));
                            generalForecast.setNightTempMax(Integer.parseInt(night.getElementsByTagName("tempmax").item(0).getTextContent()));
                            generalForecast.setNightText(night.getElementsByTagName("text").item(0).getTextContent());

                            weatherDataRepository.save(generalForecast);
                        }
                        NodeList windList = forecast.getElementsByTagName("wind");
                        for (int l = 0; l < windList.getLength(); l++) {
                            Element wind = (Element) windList.item(l);

                            Wind windForecast = new Wind();
                            windForecast.setDirection(wind.getElementsByTagName("direction").item(0).getTextContent());
                            windForecast.setName(wind.getElementsByTagName("name").item(0).getTextContent());
                            windForecast.setSpeedMin(Integer.parseInt(wind.getElementsByTagName("speedmin").item(0).getTextContent()));
                            windForecast.setSpeedMax(Integer.parseInt(wind.getElementsByTagName("speedmax").item(0).getTextContent()));

                            windDataRepository.save(windForecast);

                        }
                        NodeList placeList = forecast.getElementsByTagName("place");
                        for (int k = 0; k < placeList.getLength(); k++) {
                            Element place = (Element) placeList.item(k);

                            Place placeForecast = new Place();
                            placeForecast.setName(place.getElementsByTagName("name").item(0).getTextContent());
                            placeForecast.setPhenomenon(place.getElementsByTagName("phenomenon").item(0).getTextContent());
                            placeForecast.setTempMin(Integer.parseInt(place.getElementsByTagName("tempmin").item(0).getTextContent()));

                            placesDataRepository.save(placeForecast);

                        }
                        }
                }
                    }




        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        LOGGER.info("Importing weather data");
    }
}
