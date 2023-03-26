# WeatherForecast


## Introduction:

The SpringBoot-Gradle-H2-Vue Weather Forecast Application is a web-based single-page application that uses an XML API provided by the Estonian weather service to display the current weather forecast to the user in a user-friendly format. The application also has a background job that runs once an hour and calculates the average minimum and maximum temperature across all locations in Estonia for the current date.

## Technology Stack:

* Java 17 or higher
* Node.js 18.0 or higher
* Gradle
* SpringBoot
* H2 database
* Vue.js

## Usage:

* Clone the project from the GitHub repository.
* Open the project in your IDE.
* Run the SpringBoot application.
* Open a web browser and go to http://localhost:8080 to view the weather forecast.
* The weather forecast will be displayed in a user-friendly format on the webpage.
* The background job that runs once an hour to calculate the average minimum and maximum temperature across all locations in Estonia for the current date will run automatically.


##Code Structure:

* The controller package contains the WeatherForecastController class, which handles incoming HTTP requests.
* The entity package contains the GeneralForecast, Place, and Wind entities, which are used to represent data retrieved from the weather API.
* The repository package contains the PlacesDataRepository, WeatherDataRepository, and WindDataRepository classes, which handle data access to the H2 database.
* The service package contains the TemperatureAverageService, WeatherDataImportService, and WeatherForecastService classes, which provide the business logic for the application.
* The stats folder contains the temperature_stats.txt file, which stores the average minimum and maximum temperature across all locations in Estonia for the current date.
* The src/main/resources/templates folder contains the index.html file, which serves as the single-page Vue frontend for the application.
* The tests folder contains the JUnit test classes for the application.




