package com.example.minesweeper.service;

import com.example.minesweeper.dto.GameInfoResponse;
import com.example.minesweeper.model.MinesweeperGame;
import com.example.minesweeper.repository.MinesweeperGameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GameService {
    private MinesweeperGameRepository gameRepository;

    @Autowired
    public GameService(MinesweeperGameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Transactional
    public GameInfoResponse turn(String gameId, int row, int column) {
        MinesweeperGame game = gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("No such game id"));
        if (row >= game.getHeight() || column >= game.getWidth()) {
            throw new IllegalArgumentException("There is no such cell on the field");
        }

        Minesweeper minesweeper = new Minesweeper(mapDbToService(game.getField()), game.getState());
        MinesweeperPlayerView minesweeperPlayer = new MinesweeperPlayerView(minesweeper);
        minesweeperPlayer.turn(row, column);

        game.updateField(mapServiceToDb(minesweeper.getField()));
        game.setState(minesweeper.getState());
        gameRepository.save(game);

        GameInfoResponse body = new GameInfoResponse();
        body.setGameId(gameId);
        body.setHeight(minesweeperPlayer.getHeight());
        body.setWidth(minesweeperPlayer.getWidth());
        body.setField(minesweeperPlayer.getField());
        body.setMinesCount(minesweeperPlayer.getMinesCount());
        body.setCompleted(minesweeperPlayer.isCompleted());

        return body;
    }

    @Transactional
    public GameInfoResponse createGame(int height, int width, int minesCount) {
        Minesweeper minesweeper = new Minesweeper(height, width, minesCount);
        MinesweeperPlayerView minesweeperPlayer = new MinesweeperPlayerView(minesweeper);

        MinesweeperGame game = new MinesweeperGame();
        game.setField(mapServiceToDb(minesweeper.getField()));
        game.setHeight(minesweeper.getHeight());
        game.setWidth(minesweeper.getWidth());
        game.setState(minesweeper.getState());
        game.setMinesCount(minesweeper.getMinesCount());
        gameRepository.save(game);

        GameInfoResponse body = new GameInfoResponse();
        body.setGameId(game.getId());
        body.setHeight(minesweeperPlayer.getHeight());
        body.setWidth(minesweeperPlayer.getWidth());
        body.setField(minesweeperPlayer.getField());
        body.setMinesCount(minesweeperPlayer.getMinesCount());
        body.setCompleted(minesweeperPlayer.isCompleted());

        return body;
    }

    private Cell[][] mapDbToService(com.example.minesweeper.model.Cell[][] dbField) {
        Cell[][] serviceField = new Cell[dbField.length][dbField[0].length];
        for (int i = 0; i < serviceField.length; i++) {
            for (int j = 0; j < serviceField[0].length; j++) {
                serviceField[i][j] = new Cell(i, j, dbField[i][j].getValue(), dbField[i][j].getOpen());
            }
        }

        return serviceField;
    }

    private com.example.minesweeper.model.Cell[][] mapServiceToDb(Cell[][] serviceField) {
        com.example.minesweeper.model.Cell[][] dbField =
                new com.example.minesweeper.model.Cell[serviceField.length][serviceField[0].length];
        for (int i = 0; i < serviceField.length; i++) {
            for (int j = 0; j < serviceField[0].length; j++) {
                com.example.minesweeper.model.Cell dbCell = new com.example.minesweeper.model.Cell();
                dbCell.setRow(i);
                dbCell.setColumn(j);
                dbCell.setValue(serviceField[i][j].value);
                dbCell.setOpen(serviceField[i][j].open);
                dbField[i][j] = dbCell;
            }
        }

        return dbField;
    }
}
