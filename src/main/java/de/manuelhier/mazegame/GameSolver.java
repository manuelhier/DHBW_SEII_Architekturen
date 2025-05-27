package de.manuelhier.mazegame;

import org.openapitools.client.model.DirectionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class GameSolver {

    private static final Boolean DEBUG_MODE = true;
    private static final Logger LOGGER = LoggerFactory.getLogger(GameSolver.class);

    Game game;



    public GameSolver(Game game) {
        this.game = game;
    }

    public void solve() {
        EnumSet<DirectionDto> directions = allowedDirections();

        while (!directions.isEmpty()) {
            directions = moveToNextIntersection(directions);
            print(game);
            print("Next: " + directions);
        }
        print("Maze solved.");

    }

//    private boolean move(DirectionDto direction) {
//        if (game.step(direction)) {
//            return move(direction);
//        } else {
//            return false;
//        }
//    }

    private EnumSet<DirectionDto> moveToNextIntersection(EnumSet<DirectionDto> directions) {
        DirectionDto next = directions.iterator().next();

        if (game.step(next) && !directions.isEmpty()) {
            EnumSet<DirectionDto> allowedDirections = allowedDirections();

            // Remove Reverse Direction
            allowedDirections.remove(getReverseDirection(next));

            if (allowedDirections.size() > 1) {
                game.visitedIntersections.add(new Game.Position(game.positionX, game.positionY));
                return allowedDirections;
            }

            return moveToNextIntersection(allowedDirections);
        }
        return EnumSet.noneOf(DirectionDto.class);
    }

    private EnumSet<DirectionDto> allowedDirections() {
        EnumSet<DirectionDto> allowedDirections = EnumSet.allOf(DirectionDto.class);
        Iterator<DirectionDto> iterator = allowedDirections.iterator();

        while (iterator.hasNext()) {
            DirectionDto direction = iterator.next();

            StringBuilder debugMessage = new StringBuilder();
            debugMessage.append("Checking ").append(direction.name()).append(": ");

            if (game.moveIsNotAllowed(direction)) {
                iterator.remove();
                print(debugMessage.append("Not allowed."));
                continue;
            }

            if (game.step(direction)) {
                print(debugMessage.append("Reverting move."));
                game.step(getReverseDirection(direction));
            } else {
                iterator.remove();
            }
        }

        return allowedDirections;
    }

    private DirectionDto getReverseDirection(DirectionDto direction) {
        return switch (direction) {
            case RIGHT -> DirectionDto.LEFT;
            case LEFT -> DirectionDto.RIGHT;
            case DOWN -> DirectionDto.UP;
            case UP -> DirectionDto.DOWN;
        };
    }

    public void print(Object obj) {
        if (DEBUG_MODE) {
            LOGGER.info(String.valueOf(obj));
        }
    }

    public void print(String s) {
        if (DEBUG_MODE) {
            LOGGER.info(String.valueOf(s));
        }
    }
}
