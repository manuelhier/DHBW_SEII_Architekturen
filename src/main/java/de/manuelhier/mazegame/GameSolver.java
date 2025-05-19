package de.manuelhier.mazegame;

public class GameSolver {

//    public GameDto solveGame(int gameId) {
//        PositionDto currentPosition = loadGame(gameId).getPosition();
//
//        if (currentPosition != null) {
//
//            assert currentPosition.getPositionX() != null;
//            assert currentPosition.getPositionY() != null;
//
//            if (currentPosition.getPositionX().compareTo(BigDecimal.valueOf(5)) == 0) {
//                if (currentPosition.getPositionY().compareTo(BigDecimal.valueOf(5)) == 0) {
//                    return loadGame(gameId);
//                }
//            }
//
//            if (moveIsNotAllowed(DirectionDto.RIGHT, currentPosition)) {
//                if (moveIsNotAllowed(DirectionDto.UP, currentPosition)) {
//                    if (moveIsNotAllowed(DirectionDto.LEFT, currentPosition)) {
//                        if (moveIsNotAllowed(DirectionDto.DOWN, currentPosition)) {
//                            return null;
//                        } else {
//                            move(gameId, DirectionDto.DOWN);
//                            return solveGame(gameId);
//                        }
//                    } else {
//                        move(gameId, DirectionDto.LEFT);
//                        return solveGame(gameId);
//                    }
//                } else {
//                    move(gameId, DirectionDto.UP);
//                    return solveGame(gameId);
//                }
//            } else {
//                move(gameId, DirectionDto.RIGHT);
//                return solveGame(gameId);
//            }
//
//
//        }
//        return null;
//    }
}
