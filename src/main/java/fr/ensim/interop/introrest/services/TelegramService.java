package fr.ensim.interop.introrest.services;

import fr.ensim.interop.introrest.model.telegram.ApiResponseTelegram;
import fr.ensim.interop.introrest.model.telegram.ApiResponseUpdateTelegram;
import fr.ensim.interop.introrest.model.telegram.Update;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

@Service
public class TelegramService {
    @Value("${telegram.api.url}")
    private String telegramApiUrl;

    @Value("${telegram.bot.id}")
    private String telegramBotId;
    @Value("${telegram.chat.id}")
    private String telegramChatId;
    private List<Update> updates = new ArrayList<>();





    public ApiResponseTelegram sendMessage(RestTemplate restTemplate, String message) {
        String SENDMESSAGE = telegramApiUrl + telegramBotId + "/sendMessage"+"?chat_id="+telegramChatId+"&text="+message;

        ApiResponseTelegram response = restTemplate.getForObject(SENDMESSAGE, ApiResponseTelegram.class);

        return response;

    }

    public ApiResponseUpdateTelegram getUpdate() {
        // Operation de pooling pour capter les evenements Telegram
        //Setting up the request
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(headers);
        String GETUPDATES = telegramApiUrl + telegramBotId + "/getUpdates";


        //POST: /getUpdates
        ResponseEntity<ApiResponseUpdateTelegram> response = restTemplate.postForEntity(GETUPDATES, request, ApiResponseUpdateTelegram.class);
        ApiResponseUpdateTelegram res = response.getBody();

        return res;

    }
    /*ApiResponseUpdateTelegram res = telegramService.getUpdate();

                String latestMessage = res.getResult().get(res.getResult().size()-1).getMessage().getText();*/

    public String getChatId() {
        // Fetch the updates from the Telegram API
        ApiResponseUpdateTelegram response = getUpdate();
        // Update the local list of updates with the latest data from the API
        updates = response.getResult();
        // Get the chat ID from the most recent update
        String chatId = updates.get(updates.size() - 1).getMessage().getChat().getId().toString();
        return chatId;
    }


    public String getLatestMessage(){

        ApiResponseUpdateTelegram res = getUpdate();
        return res.getResult().get(res.getResult().size()-1).getMessage().getText();

    }



}