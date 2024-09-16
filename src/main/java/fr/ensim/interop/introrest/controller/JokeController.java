package fr.ensim.interop.introrest.controller;

import fr.ensim.interop.introrest.model.Joke;
import fr.ensim.interop.introrest.services.JokeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JokeController {

    // The JokeService object is used to fetch jokes from an external API.
    private final JokeService jokeService;

    // This method handles GET requests to "/joke" and returns a random joke.
    @GetMapping("/joke")
    public ResponseEntity<Joke> joke() {
        // Fetch a random joke from the JokeService.
        Joke joke = jokeService.getJoke();

        // Return the fetched joke as a JSON object.
        return ResponseEntity.ok().body(joke);
    }
}
