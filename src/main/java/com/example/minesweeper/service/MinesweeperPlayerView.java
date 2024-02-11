package com.example.minesweeper.service;

import java.util.List;


/**
 * The class represents player's point of view for a game state, i.e. cells can be
 * closed or have some number, in case of win or lose the class renders the
 * field opening all non-mine cells and displaying mines as X or M.
 */
public class MinesweeperPlayerView {
    private Minesweeper minesweeper;
    private char[][] field;

    public MinesweeperPlayerView(Minesweeper minesweeper) {
        this.minesweeper = minesweeper;
        Cell[][] cellsField = minesweeper.getField();
        this.field = new char[minesweeper.getHeight()][minesweeper.getWidth()];
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {
                if (cellsField[i][j].open) {
                    field[i][j] = cellsField[i][j].value;
                } else {
                    field[i][j] = ' ';
                }
            }
        }
    }

    public void turn(int row, int column) {
        List<Cell> openedCells = minesweeper.turn(row, column);
        for (Cell cell : openedCells) {
            field[cell.row][cell.column] = cell.value;
        }
    }

    public int getHeight() {
        return minesweeper.getHeight();
    }

    public int getWidth() {
        return minesweeper.getWidth();
    }

    public char[][] getField() {
        if (minesweeper.getState() == State.WON) {
            for (int i = 0; i < field.length; i++) {
                for (int j = 0; j < field[0].length; j++) {
                    if (field[i][j] == 'X') {
                        field[i][j] = 'M';
                    }
                }
            }
        }

        return field;
    }

    public int getMinesCount() {
        return minesweeper.getMinesCount();
    }

    public boolean isCompleted() {
        return minesweeper.getState() != State.PLAYING;
    }
}
