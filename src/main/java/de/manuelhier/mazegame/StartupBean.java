package de.manuelhier.mazegame;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class StartupBean {

    @PostConstruct
    public void init() {

        Game game = new Game(542);
        GameSolver gameSolver = new GameSolver(game);

        gameSolver.solve();

    }

}
