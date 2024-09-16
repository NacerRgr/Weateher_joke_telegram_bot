package fr.ensim.interop.introrest;

import fr.ensim.interop.introrest.model.Joke;
import fr.ensim.interop.introrest.model.telegram.ApiResponseUpdateTelegram;
import fr.ensim.interop.introrest.model.telegram.Update;
import fr.ensim.interop.introrest.model.weather_api_models.WeatherData;
import fr.ensim.interop.introrest.services.JokeService;
import fr.ensim.interop.introrest.services.TelegramService;
import fr.ensim.interop.introrest.services.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
public class ListenerUpdateTelegram implements CommandLineRunner {

    @Value("${telegram.api.url}")
    private String telegramApiUrl;

    @Value("${telegram.bot.id}")
    private String telegramBotId;

    @Value("${open.weather.api.url}")
    private String openWeatherMapApiUrl;

    @Value("${open.weather.api.token}")
    private String openWeatherMapToken;

    private final TelegramService telegramService;
    private final WeatherService weatherService;
    private final JokeService jokeService;

    List<Update> messages = new ArrayList<>();

    @Autowired
    public ListenerUpdateTelegram(TelegramService telegramService, WeatherService weatherService,JokeService jokeService) {
        this.telegramService = telegramService;
        this.weatherService = weatherService;
        this.jokeService = jokeService;
    }

    @Override
    public void run(String... args) throws Exception {
        Logger.getLogger("ListenerUpdateTelegram").log(Level.INFO, "Démarage du listener d'updates Telegram...");

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            while (true) {

                HttpEntity<String> request = new HttpEntity<>(headers);

                String GETUPDATES = telegramApiUrl + telegramBotId + "/getUpdates";

                //POST: /getUpdates
              //  ResponseEntity<ApiResponseUpdateTelegram> response = restTemplate.postForEntity(GETUPDATES, request, ApiResponseUpdateTelegram.class);
                ApiResponseUpdateTelegram res = telegramService.getUpdate();

                String latestMessage = res.getResult().get(res.getResult().size()-1).getMessage().getText();
                String commande = latestMessage.split(":")[0];

                if (res.getResult().size() > 0){
                    if (messages.size() != res.getResult().size()){

                        messages = res.getResult();
                        System.out.println("New message : " + latestMessage);
                        System.out.println(latestMessage);

                        switch (commande) {
                            case "/start": {
                                welcomeUser(new RestTemplate());//send messqge
                                break;
                            }
                            case "/weather": {
                                String city = extractCity(latestMessage);
                                if (city != null) {
                                    String temp =weatherService.getWeatherTemperature(city);

                                    telegramService.sendMessage(new RestTemplate(),temp);
                                } else {
                                    telegramService.sendMessage(new RestTemplate(),"Please provide a city for the weather report.");
                                }
                                break;
                            }
                            case "/joke": {
                                Joke joke = jokeService.getJoke();
                                String jokeTosend = joke.getSetup() + "\n\n" +  joke.getDelivery() ;
                                telegramService.sendMessage(new RestTemplate(),jokeTosend);
                                break;
                            }
                            default: {
                                String errorMessage = "The command : " + latestMessage.split(" ")[0] + " is not known";
                                System.out.println("Error: " + errorMessage);
                                telegramService.sendMessage(new RestTemplate(), errorMessage);
                                break;
                            }
                        }
                    }

                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }


        } catch (Exception e) {
        }
    }

    // Operation de pooling pour capter les evenements Telegram
    public void welcomeUser(RestTemplate restTemplate) {
        StringBuilder sb = new StringBuilder();

        sb.append("Bonjour, je suis EnsimBot ! \n\n");
        sb.append("Je suis un chatbot créé par Hamza et Nacer\n\n");

        sb.append("Je propose 2 services principaux : \n\n");
        sb.append("- Obtenir la température météorologique : /weather:city\n");
        sb.append("- Écouter une blague : /joke\n");

        telegramService.sendMessage(restTemplate, sb.toString());
    }

    private String extractCity(String message) {
        Pattern pattern = Pattern.compile("/weather:(\\w+)");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
