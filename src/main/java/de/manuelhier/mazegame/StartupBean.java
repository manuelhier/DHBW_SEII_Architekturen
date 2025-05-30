package de.manuelhier.mazegame;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class StartupBean {

    @PostConstruct
    public void init() {
        //        Instant Solution:
        //        game.move(DirectionDto.UP);
        //        game.move(DirectionDto.RIGHT);
        //        game.move(DirectionDto.DOWN);
        //        game.move(DirectionDto.RIGHT);
        //        game.move(DirectionDto.UP);

        // Initialize a new Game
        Game game = new Game();
        System.out.println(game);

        // Initialize a GameSolver class for new Game
        GameSolver gameSolver = new GameSolver(game);
        gameSolver.solve(); // Solve the Game

        System.out.println(game);

        //        Read some usage stats for fun
        //        int counter = 0;
        //        HashMap<String, Integer> stats = new HashMap<>();
        //        while (true) {
        //            Game game = new Game(counter++);
        //            System.out.println("Game " + counter);
        //            if (game.game != null) {
        //                String groupName = game.game.getGroupName();
        //                if (stats.containsKey(groupName)) {
        //                    stats.put(groupName, stats.get(groupName) + 1);
        //                } else {
        //                    stats.put(groupName, 1);
        //                } else{
        //                    break;
        //                }
        //            }
        //            System.out.println(stats);
        //        }
    }
}
