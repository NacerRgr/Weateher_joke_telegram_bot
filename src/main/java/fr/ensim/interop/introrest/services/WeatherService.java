package fr.ensim.interop.introrest.services;

import fr.ensim.interop.introrest.model.weather_api_models.WeatherData;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.text.SimpleDateFormat;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Service
public class WeatherService {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(WeatherService.class);


    @Value("${open.weather.api.url}")
    private String openWeatherMapApiUrl;

    @Value("${open.weather.api.token}")
    private String openWeatherMapToken;

    public WeatherData getWeather(String city) {
        String url = openWeatherMapApiUrl
                + "?q=" + city
                + "&appid=" + openWeatherMapToken
                + "&units=metric";

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, WeatherData.class);
    }

   /* public String getWeatherTemperature(String city) {
        WeatherData weatherData = getWeather(city);
        // extract the temp from the weatherData
        System.out.println(weatherData.getMain().getTemperature().toString());
        return weatherData.getMain().getTemperature().toString();



    }*/
   public String getWeatherTemperature(String city) {
       WeatherData weatherData = getWeather(city);
       if (weatherData == null || weatherData.getMain() == null) {
           logger.warn("⚠️ Weather data or main information is null for city: {}", city);
           return "❌ Weather data not available";
       }

       String cityName = weatherData.getCityName();
       double temperature = weatherData.getMain().getTemperature();
       String weatherDescription = (weatherData.getWeather() != null && weatherData.getWeather().length > 0)
               ? weatherData.getWeather()[0].getDescription()
               : "No description available";
       double windSpeed = weatherData.getWind().getSpeed();
       String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(weatherData.getDateTime() * 1000));

       return String.format("☁️ Weather report for %s:\n🌡️ Temperature: %.2f°C\n📝 Description: %s\n💨 Wind Speed: %.2f m/s\n🕒 Date and Time: %s",
               cityName, temperature, weatherDescription, windSpeed, dateTime);
   }
}

