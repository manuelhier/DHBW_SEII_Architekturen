package de.manuelhier.mazegame;

import jakarta.annotation.PostConstruct;
import org.openapitools.client.api.DefaultApi;
import org.openapitools.client.model.GameDto;
import org.openapitools.client.model.GameInputDto;
import org.springframework.stereotype.Component;

@Component
public class StartupBean {

    @PostConstruct
    public void init() {
        DefaultApi defaultApi = new DefaultApi();

        GameInputDto gameInput = new GameInputDto();
        gameInput.setGroupName("testGame");

        GameDto response = defaultApi.gamePost(gameInput);

        System.out.println(response);

        // Game: 126
    }
}
