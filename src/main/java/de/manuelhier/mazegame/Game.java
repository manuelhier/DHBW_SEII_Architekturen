package de.manuelhier.mazegame;

import org.openapitools.client.api.DefaultApi;
import org.openapitools.client.model.*;

import java.math.BigDecimal;

public class Game {

    private static final int GRID_MIN = 1;
    private static final int GRID_MAX = 5;

    static DefaultApi GameApi = new DefaultApi();
    static String groupName = "TestGroupAlpha";

    protected GameDto game;
    protected int gameId;
    protected int posX, posY;

    public Game() {
        GameInputDto gameInput = new GameInputDto();
        gameInput.setGroupName(groupName);

        this.game = GameApi.gamePost(gameInput);
        this.gameId = game.getGameId() != null ? game.getGameId().intValue() : 0;
        updateGameState();
    }

    public Game(int gameId) {
        try {
            this.gameId = gameId;
            updateGameState();
        } catch (Exception e) {
            this.game = null;
        }
    }

    public void updateGameState() {
        // Get & update current game state
        this.game = GameApi.gameGameIdGet(BigDecimal.valueOf(gameId));

        // Asserts needed to suppress warnings!
        assert game.getPosition() != null;
        assert game.getPosition().getPositionX() != null;
        assert game.getPosition().getPositionY() != null;

        // Update Position
        this.posX = game.getPosition().getPositionX().intValue();
        this.posY = game.getPosition().getPositionY().intValue();
    }

    public boolean step(DirectionDto direction) {
        if (game != null && game.getStatus() == GameStatusDto.ONGOING && stepIsAllowed(direction)) {
            MoveInputDto moveInputDto = new MoveInputDto().direction(direction);

            // Make step
            MoveDto result = GameApi.gameGameIdMovePost(BigDecimal.valueOf(this.gameId), moveInputDto);
            updateGameState();

            // Print move status
            assert result.getMoveStatus() != null;
            return result.getMoveStatus() == MoveStatusDto.MOVED;
        }
        return false;
    }

    public boolean move(DirectionDto direction) {
        if (this.step(direction)) {
            return move(direction);
        } else {
            return false;
        }
    }

    private boolean stepIsAllowed(DirectionDto direction) {
        // Save current position
        int x = posX;
        int y = posY;

        // Simulate new position based on move direction
        switch (direction) {
            case UP -> y++;
            case DOWN -> y--;
            case LEFT -> x--;
            case RIGHT -> x++;
        }

        // Testing restrictions / boundaries
        // if (x == 5 && y == 4) return false;
        // if (x == 4 && y == 3) return false;
        // if (x == 4 && y == 1) return false;

        // Check if simulated position is out of bounds
        return (x >= GRID_MIN && x <= GRID_MAX && y >= GRID_MIN && y <= GRID_MAX);
    }

    public boolean isFinished() {
        return game.getStatus() == GameStatusDto.SUCCESS && posX == 5 && posY == 5;
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
