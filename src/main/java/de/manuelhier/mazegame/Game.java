package de.manuelhier.mazegame;

import org.openapitools.client.api.DefaultApi;
import org.openapitools.client.model.*;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Game {

    static DefaultApi GameApi = new DefaultApi();
    static String groupName = "TestGroupAlpha";

    private static GameDto createNewGame() {
        GameInputDto gameInput = new GameInputDto();
        gameInput.setGroupName(groupName);
        return GameApi.gamePost(gameInput);
    }

    GameDto game;
    int gameId;
    int positionX;
    int positionY;
    GameStatusDto status;

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
        this.game = GameApi.gameGameIdGet(BigDecimal.valueOf(this.gameId));
        assert game.getPosition() != null;
        assert game.getPosition().getPositionX() != null;
        this.positionX = game.getPosition().getPositionX().intValue();
        assert game.getPosition().getPositionY() != null;
        this.positionY = game.getPosition().getPositionY().intValue();
        this.status = game.getStatus();
    }

    public void printCurrentPosition() {
        PositionDto currentPosition = game.getPosition();
        assert currentPosition != null;
        System.out.print("X:" + currentPosition.getPositionX() + " Y:" + currentPosition.getPositionY() + " ");
    }

    public boolean step(DirectionDto direction) {
        if (game != null && status == GameStatusDto.ONGOING) {
            printCurrentPosition();

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

    public boolean move(DirectionDto direction) {
        if (step(direction)) {
            return move(direction);
        } else {
            return false;
        }
    }

    private boolean moveIsNotAllowed(DirectionDto direction) {
        PositionDto currentPosition = game.getPosition();
        assert currentPosition != null;

        int x = positionX;
        int y = positionY;

        switch (direction) {
            case UP: y++; break;
            case DOWN: y--; break;
            case LEFT: x--; break;
            case RIGHT: x++; break;
        }

        // Test if position is out of bounds
        return ( x < 1 || x > 5 || y < 1 || y > 5 );
    }

    public ArrayList<DirectionDto> allowedDirections () {
        ArrayList<DirectionDto> allowedDirections = new ArrayList<>();
        for (DirectionDto direction : DirectionDto.values()) {
            if (!moveIsNotAllowed(direction)) {
                allowedDirections.add(direction);
            }
        }
        return allowedDirections;
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
