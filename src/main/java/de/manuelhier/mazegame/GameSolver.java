package de.manuelhier.mazegame;

import org.openapitools.client.model.DirectionDto;

import java.util.*;

public class GameSolver {

    Game game;

    public GameSolver(Game game) {
        this.game = game;
    }

    public void solve() {
        // Instant Solution:
        // game.move(DirectionDto.UP);
        // game.move(DirectionDto.RIGHT);
        // game.move(DirectionDto.DOWN);
        // game.move(DirectionDto.RIGHT);
        // game.move(DirectionDto.UP);

        EnumSet<DirectionDto> directions = allowedDirections();

        while (!directions.isEmpty()) {
            DirectionDto next = directions.iterator().next();
            directions = moveToNextIntersection(next);
            System.out.println(game);
            System.out.println("Next: " + directions);
        }
        System.out.println("Maze solved.");

    }

    private boolean move(DirectionDto direction) {
        if (game.step(direction)) {
            return move(direction);
        } else {
            return false;
        }
    }

    private EnumSet<DirectionDto> moveToNextIntersection(DirectionDto direction) {
        if (game.step(direction)) {
            EnumSet<DirectionDto> allowedDirections = allowedDirections();

            // Remove Reverse Direction
            switch (direction) {
                case UP -> allowedDirections.remove(DirectionDto.DOWN);
                case DOWN -> allowedDirections.remove(DirectionDto.UP);
                case LEFT -> allowedDirections.remove(DirectionDto.RIGHT);
                case RIGHT -> allowedDirections.remove(DirectionDto.LEFT);
            }

            if (allowedDirections.size() > 1) {
                game.visitedIntersections.add(new Game.Position(game.positionX, game.positionY));
                return allowedDirections;
            }

            return moveToNextIntersection(allowedDirections.iterator().next());
        }
        return EnumSet.noneOf(DirectionDto.class);
    }

    private EnumSet<DirectionDto> allowedDirections() {
        EnumSet<DirectionDto> allowedDirections = EnumSet.allOf(DirectionDto.class);
        Iterator<DirectionDto> iterator = allowedDirections.iterator();

        while (iterator.hasNext()) {
            DirectionDto direction = iterator.next();
            System.out.print("Checking " + direction.name() + ": ");

            if (game.moveIsNotAllowed(direction)) {
                iterator.remove();
                System.out.println("Not allowed.");
                continue;
            }

            if (game.step(direction)) {
                System.out.print("Reverting " + direction.name() + ": ");
                switch (direction) {
                    case RIGHT -> game.step(DirectionDto.LEFT);
                    case LEFT -> game.step(DirectionDto.RIGHT);
                    case DOWN -> game.step(DirectionDto.UP);
                    case UP -> game.step(DirectionDto.DOWN);
                }
            } else {
                iterator.remove();
            }
        }

        return allowedDirections;
    }
}
