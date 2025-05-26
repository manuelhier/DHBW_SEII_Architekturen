package de.manuelhier.mazegame;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class StartupBean {

    @PostConstruct
    public void init() {

        int counter = 1;

        while(true) {
            Game game = new Game(counter);

            if (game.game != null) {
                GameSolver gameSolver = new GameSolver(game);
                gameSolver.solve();
                counter++;
            } else {
                break;
            }

        }

    }

}
