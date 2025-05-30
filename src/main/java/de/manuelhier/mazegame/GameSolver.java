package de.manuelhier.mazegame;

import org.openapitools.client.model.DirectionDto;

import java.util.*;

public class GameSolver {

    private final Game gameInstance;

    protected record Position(int x, int y) { }

    // GameSolver Data
    protected Set<Position> visitedPositions = new HashSet<>();
    protected HashMap<Position, EnumSet<DirectionDto>> intersections = new HashMap<>();
    protected Stack<DirectionDto> steps = new Stack<>();

    public GameSolver(Game game) {
        this.gameInstance = game;
    }

    public void solve() {
        EnumSet<DirectionDto> directions = getAllowedDirections();

        while (!directions.isEmpty()) {
            directions = moveToNextIntersection(directions);
            System.out.println("Next Directions: " + directions);
        }

        System.out.println("-------------------------------");
        System.out.println("Intersections: " + intersections);
        System.out.println("Steps: " + steps);

        if (gameInstance.isFinished()) {
            System.out.println("Maze solved.");
        } else {
            System.out.println("Maze could not be solved.");
        }
    }

    private EnumSet<DirectionDto> moveToNextIntersection(EnumSet<DirectionDto> directions) {
        if (directions.isEmpty()) return EnumSet.noneOf(DirectionDto.class);
        DirectionDto nextDirection = directions.iterator().next();

        if (!gameInstance.step(nextDirection) && !gameInstance.isFinished()) {
            // Step not possible -> Remove current direction and try next
            directions.remove(nextDirection);
            return moveToNextIntersection(directions);
        }

        // Step was successful -> Save new position
        steps.push(nextDirection);
        Position currentPos = new Position(gameInstance.posX, gameInstance.posY);
        System.out.println(nextDirection + " -> " + "x: " + currentPos.x + ", y: " + currentPos.y);

        if (!visitedPositions.add(currentPos)) {
            // Cycle detected -> Position already visited
            directions.remove(nextDirection);

            if (directions.isEmpty()) {
                System.out.println("Cycle detected. No next intersection.");
                return EnumSet.noneOf(DirectionDto.class);
            } else {
                System.out.println("Got to next intersection." + directions);
                return moveToNextIntersection(directions);
            }
        }

        // Check if game is finished
        if (gameInstance.isFinished()) {
            return EnumSet.noneOf(DirectionDto.class);
        }

        directions = getAllowedDirections();
        directions.remove(getReverseDirection(nextDirection));

        // Detect Intersection
        if (directions.size() > 1) {
            intersections.putIfAbsent(currentPos, directions);
            System.out.println("INTERSECTION : " + intersections.get(currentPos));
            return directions;
        }

        // Detect Dead-end
        if (directions.isEmpty()) {
            System.out.println("DEAD-END : Going back to last intersection");
            return goBackToLastIntersection();
        }
        return moveToNextIntersection(directions);
    }

    private EnumSet<DirectionDto> goBackToLastIntersection() {
        if (steps.isEmpty()) return EnumSet.noneOf(DirectionDto.class);
        DirectionDto reverseDirection = getReverseDirection(steps.pop());

        if (gameInstance.step(reverseDirection)) {
            System.out.println(reverseDirection + " -> " + "x: " + gameInstance.posX + ", y: " + gameInstance.posY);
            Position pos = new Position(gameInstance.posX, gameInstance.posY);
            EnumSet<DirectionDto> intersection = intersections.get(pos);

            // Detect intersection
            if (intersection != null) {
                System.out.println("Found intersection. Intersection: " + intersection);
                intersection.remove(getReverseDirection(reverseDirection));
                return intersection;
            }
        }
        return goBackToLastIntersection();
    }

    private EnumSet<DirectionDto> getAllowedDirections() {
        EnumSet<DirectionDto> allowed = EnumSet.noneOf(DirectionDto.class);

        // Iterate through all available directions (up, down, left, right)
        for (DirectionDto direction : DirectionDto.values()) {

            // If check is possible add to allowed list
            if (gameInstance.step(direction)) {
                gameInstance.step(getReverseDirection(direction));
                allowed.add(direction);
            }
        }
        return allowed;
    }

    private DirectionDto getReverseDirection(DirectionDto direction) {
        return switch (direction) {
            case RIGHT -> DirectionDto.LEFT;
            case LEFT -> DirectionDto.RIGHT;
            case DOWN -> DirectionDto.UP;
            case UP -> DirectionDto.DOWN;
        };
    }
}
