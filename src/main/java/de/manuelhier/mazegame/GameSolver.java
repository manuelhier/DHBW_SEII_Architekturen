package de.manuelhier.mazegame;

import org.openapitools.client.model.DirectionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class GameSolver {

    private static final Boolean DEBUG_MODE = true;
    private static final Logger LOGGER = LoggerFactory.getLogger(GameSolver.class);

    Game game;

    protected record Position(int x, int y) {
    }

    protected Set<Position> visitedPositions = new HashSet<>();
    protected HashMap<Position, EnumSet<DirectionDto>> intersections = new HashMap<>();
    protected Stack<DirectionDto> steps = new Stack<>();


    public GameSolver(Game game) {
        this.game = game;
    }

    public void solve() {
        EnumSet<DirectionDto> directions = getAllowedDirections();

        while (!directions.isEmpty()) {
            directions = moveToNextIntersection(directions);
            print("Next: " + directions);
        }

        if (game.isFinished()) {
            print("Maze solved.");
        }

        print("Visited Intersections: " + intersections);
        print("Steps since last intersection: " + steps);
    }

    private EnumSet<DirectionDto> moveToNextIntersection(EnumSet<DirectionDto> directions) {
        DirectionDto next = directions.iterator().next();

        Position nextPos = getNextPosition(game.posX, game.posY, next);
        if (visitedPositions.contains(nextPos)) {
            directions.remove(next);
            if (!directions.isEmpty()) {
                print("Find next intersection: " + nextPos);
                return moveToNextIntersection(directions);
            } else {
                print("Cycle detected. No next intersection. Maze can not be solved.");
                return EnumSet.noneOf(DirectionDto.class);
            }
        }

        if (game.step(next)) {
            print(next + " -> " + "x: " + game.posX + ", y: " + game.posY);
            visitedPositions.add(nextPos);
            steps.push(next);

            // Check if game is finished
            if (game.isFinished()) {
                return EnumSet.noneOf(DirectionDto.class);
            }

            directions = getAllowedDirections();
            directions.remove(getReverseDirection(next));

            // Detect Intersection
            if (directions.size() > 1) {
                Position pos = new Position(game.posX, game.posY);
                intersections.putIfAbsent(pos, directions);
                print("INTERSECTION : " + intersections.get(pos));
                return directions;
            }

            // Detect Dead-end
            if (directions.isEmpty()) {
                print("DEAD-END : Going back to last intersection");
                return tracebackToIntersection();
            }
            return moveToNextIntersection(directions);
        }
        return EnumSet.noneOf(DirectionDto.class);
    }

    private EnumSet<DirectionDto> tracebackToIntersection() {
        DirectionDto next = getReverseDirection(steps.pop());

        if (game.step(next)) {
            print(next + " -> " + "x: " + game.posX + ", y: " + game.posY);
            Position pos = new Position(game.posX, game.posY);
            EnumSet<DirectionDto> intersection = intersections.get(pos);

            // Detect intersection
            if (intersection != null) {
                print("Found intersection. Intersection: " + intersection);
                intersection.remove(getReverseDirection(next));
                return intersection;
            }
        }
        return tracebackToIntersection();
    }

    private EnumSet<DirectionDto> getAllowedDirections() {
        EnumSet<DirectionDto> allowed = EnumSet.noneOf(DirectionDto.class);

        for (DirectionDto direction : DirectionDto.values()) {
            if ((game.posX == 4 && game.posY == 5 && direction == DirectionDto.RIGHT) ||
                    (game.posX == 5 && game.posY == 4 && direction == DirectionDto.UP)) {
                allowed.add(direction);
                continue;
            }

            if (game.step(direction)) {
                game.step(getReverseDirection(direction));
                allowed.add(direction);
            }
        }
        return allowed;
    }

    private Position getNextPosition(int x, int y, DirectionDto direction) {
        return switch (direction) {
            case UP -> new Position(x, y + 1);
            case DOWN -> new Position(x, y - 1);
            case LEFT -> new Position(x - 1, y);
            case RIGHT -> new Position(x + 1, y);
        };
    }


    private DirectionDto getReverseDirection(DirectionDto direction) {
        return switch (direction) {
            case RIGHT -> DirectionDto.LEFT;
            case LEFT -> DirectionDto.RIGHT;
            case DOWN -> DirectionDto.UP;
            case UP -> DirectionDto.DOWN;
        };
    }

    private void print(Object obj) {
        if (DEBUG_MODE) {
            LOGGER.info(String.valueOf(obj));
        }
    }

    private void print(String s) {
        if (DEBUG_MODE) {
            LOGGER.info(String.valueOf(s));
        }
    }
}
