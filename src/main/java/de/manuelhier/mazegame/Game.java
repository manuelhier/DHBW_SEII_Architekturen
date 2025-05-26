package de.manuelhier.mazegame;

import org.openapitools.client.api.DefaultApi;
import org.openapitools.client.model.*;

import java.math.BigDecimal;
import java.util.*;

public class Game {

    private static final int GRID_MIN = 1;
    private static final int GRID_MAX = 5;

    static DefaultApi GameApi = new DefaultApi();
    static String groupName = "TestGroupAlpha";

    private static GameDto createNewGame() {
        GameInputDto gameInput = new GameInputDto();
        gameInput.setGroupName(groupName);
        return GameApi.gamePost(gameInput);
    }

    protected GameDto game;
    private GameStatusDto status;

    protected int gameId;
    protected int positionX;
    protected int positionY;

    protected record Position(int x, int y) { }

    Set<Position> visitedIntersections = new HashSet<>();

    public Game() {
        this.game = createNewGame();
        assert game.getGameId() != null;
        this.gameId = game.getGameId().intValue();
        updateGameState();
    }

    public Game(int gameId) {
        try {
            this.gameId = gameId;
            updateGameState();
            System.out.println(game);
        } catch (Exception e) {
            this.game = null;
            System.err.println("Error loading game with id=" + gameId + ". Reason: " + e.getMessage());
        }
    }

    public void updateGameState() {
        // Get & update current game state
        this.game = GameApi.gameGameIdGet(BigDecimal.valueOf(this.gameId));

        // Asserts needed to suppress warnings!
        assert game.getPosition() != null;
        assert game.getPosition().getPositionX() != null;
        assert game.getPosition().getPositionY() != null;

        // Update Position & Status
        this.positionX = game.getPosition().getPositionX().intValue();
        this.positionY = game.getPosition().getPositionY().intValue();
        this.status = game.getStatus();
    }

    public boolean step(DirectionDto direction) {
        if (game != null && status == GameStatusDto.ONGOING) {

            // Print current position
            PositionDto currentPosition = game.getPosition();
            assert currentPosition != null;
            System.out.print("X:" + currentPosition.getPositionX() + " Y:" + currentPosition.getPositionY() + " ");

            // Print direction to move
            MoveInputDto moveInputDto = new MoveInputDto().direction(direction);
            System.out.print(direction.toString().toUpperCase() + " ");

            // Abort if move leads to failed status
            if (moveIsNotAllowed(direction)) {
                System.out.println("ABORT");
                return false;
            }

            // Make step
            MoveDto result = GameApi.gameGameIdMovePost(BigDecimal.valueOf(this.gameId), moveInputDto);
            updateGameState();

            // Print move status
            assert result.getMoveStatus() != null;
            System.out.println(result.getMoveStatus().toString().toUpperCase());

            return result.getMoveStatus() == MoveStatusDto.MOVED;
        }
        System.out.println("Movement not allowed. Game failed or succeeded.");
        return false;
    }

    public boolean moveIsNotAllowed(DirectionDto direction) {

        // Save current position
        int x = positionX;
        int y = positionY;

        // Simulate new position based on move direction
        switch (direction) {
            case UP -> y++;
            case DOWN -> y--;
            case LEFT -> x--;
            case RIGHT -> x++;
        }

        Position newPosition = new Position(x, y);
        if (visitedIntersections.contains(newPosition)) {
            System.out.println("HIER WAR ICH SCHON MAL");
            return true;
        }

        // Check if simulated position is out of bounds
        return (x < GRID_MIN || x > GRID_MAX || y < GRID_MIN || y > GRID_MAX);
    }

    @Override
    public String toString() {
        if (game != null) {
            return game.toString();
        } else {
            return null;
        }
    }
}
