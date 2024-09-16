package fr.ensim.interop.introrest.model.weather_api_models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrentWeather {
    @JsonProperty("temp")
    private Double temperature;

    @JsonProperty("pressure")
    private Double pressure;

    @JsonProperty("humidity")
    private Integer humidity;
}
