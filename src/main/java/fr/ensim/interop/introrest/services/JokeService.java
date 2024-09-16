package fr.ensim.interop.introrest.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ensim.interop.introrest.model.Joke;
import lombok.Value;
import org.springframework.web.client.RestTemplate;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@Service
public class JokeService {
    private String jokeURL = "https://v2.jokeapi.dev/joke/Any?lang=fr&type=twopart";

    public Joke getJoke(){
        RestTemplate restTemplate = new RestTemplate();
        Joke joke = restTemplate.getForObject(jokeURL , Joke.class);
        joke.setNote(new Random().nextInt(10)+1);
        return joke;
    }
}