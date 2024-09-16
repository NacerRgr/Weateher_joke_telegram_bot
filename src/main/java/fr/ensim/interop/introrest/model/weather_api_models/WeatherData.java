package fr.ensim.interop.introrest.model.weather_api_models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherData {
    @JsonProperty("name")
    private String cityName;

    @JsonProperty("weather")
    private Weather[] weather;

    @JsonProperty("main")
    private CurrentWeather main;

    @JsonProperty("wind")
    private Wind wind;

    @JsonProperty("dt")
    private Long dateTime;
}
