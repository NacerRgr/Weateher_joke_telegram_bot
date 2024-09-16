package fr.ensim.interop.introrest.controller;

import fr.ensim.interop.introrest.model.weather_api_models.WeatherData;
import fr.ensim.interop.introrest.services.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {

    private final WeatherService weatherService;

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/weather")
    public WeatherData getWeather(@RequestParam String city) {
        return weatherService.getWeather(city);
    }




}
