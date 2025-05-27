package de.manuelhier.mazegame;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class StartupBean {

    @PostConstruct
    public void init() {
        // Instant Solution:
        // game.move(DirectionDto.UP);
        // game.move(DirectionDto.RIGHT);
        // game.move(DirectionDto.DOWN);
        // game.move(DirectionDto.RIGHT);
        // game.move(DirectionDto.UP);

        Game game = new Game();

        GameSolver gameSolver = new GameSolver(game);

        gameSolver.solve();

        System.out.println(game);

//        int counter = 1;
//
//        while(true) {
//            Game game = new Game(counter++);
//            if (game.game != null) {
//                if (game.status != GameStatusDto.ONGOING) {
//                    System.out.println("[ GameId: " + counter + " ] SKIP. Game already over: " + game.status);
//                    continue;
//                }
//
//                GameSolver gameSolver = new GameSolver(game);
//
//                try {
//                    gameSolver.solve();
//                    System.out.println("[ GameId: " + counter + " ] Game solved: " + game.status);
//                } catch (Exception e) {
//                    System.err.println("[ GameId: " + counter + " ] Error. " + e.getMessage());
//                    break;
//                }
//            } else {
//                break;
//            }
//        }
    }
}
