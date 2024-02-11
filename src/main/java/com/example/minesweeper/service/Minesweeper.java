package com.example.minesweeper.service;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * The class represents computer's point of view for a game state, i.e. locations
 * of all mines and numbers for non-mine cells are known once an instance is created.
 *
 * @ImplNote
 * The complexity of the sum of all turns during a game cycle is O(height * width),
 * i.e. linear. Actually, it doesn't make any sense for the current implementation,
 * because at the end of the day every turn is followed by matrix traversal at the higher
 * levels (controller, service) and overall complexity degrades to quadratic.
 * <br>
 * The current class employs the following model of a game field. Every cell on the field is
 * either open or closed, at the same time every cell can have a value equal to either
 * mine (X) or number (1-8). The field's value are calculated in the class
 * constructor and are never changed during any other operations.
 */
public class Minesweeper {
    private Cell[][] field;
    private int openCellsCount;
    private int minesCount;
    private State state;

    public Minesweeper(int height, int width, int minesCount) {
        createNewField(height, width, minesCount);
        this.openCellsCount = 0;
        this.minesCount = minesCount;
        this.state = State.PLAYING;
    }

    private void createNewField(int height, int width, int minesCount) {
        field = new Cell[height][width];
        List<Cell> randomizedCells = new ArrayList<>();
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {
                Cell cell = new Cell(i, j);
                cell.open = false;
                field[i][j] = cell;
                randomizedCells.add(cell);
            }
        }

        Collections.shuffle(randomizedCells, ThreadLocalRandom.current());
        randomizedCells.stream().limit(minesCount).forEach(c -> c.value = 'X');
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {
                if (field[i][j].value != 'X') {
                    int minesAround = countNeighboringMines(field[i][j]);
                    field[i][j].value = Character.forDigit(minesAround, 10);
                }
            }
        }
    }

    public Minesweeper(Cell[][] field, State state) {
        this.field = field;
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {
                if (field[i][j].open) {
                    openCellsCount++;
                }
                if (field[i][j].value == 'X') {
                    minesCount++;
                }
            }
        }
        this.state = state;
    }

    public List<Cell> turn(int row, int column) {
        if (state != State.PLAYING) {
            throw new IllegalStateException("The game has already been completed");
        } else if (field[row][column].open) {
            throw new IllegalArgumentException("The cell has already been open");
        } else if (field[row][column].value == 'X') {
            List<Cell> openedCells = new ArrayList<>();
            for (int i = 0; i < field.length; i++) {
                for (int j = 0; j < field[0].length; j++) {
                    if (!field[i][j].open) {
                        field[i][j].open = true;
                        openedCells.add(field[i][j]);
                    }
                }
            }
            state = State.LOST;
            openCellsCount = field.length * field[0].length;

            return openedCells;
        } else {
            List<Cell> openedCells = openEmptyCell(field[row][column]);
            if (openCellsCount + minesCount == field.length * field[0].length) {
                for (int i = 0; i < field.length; i++) {
                    for (int j = 0; j < field[0].length; j++) {
                        if (!field[i][j].open) {
                            field[i][j].open = true;
                            openedCells.add(field[i][j]);
                        }
                    }
                }
                state = State.WON;
                openCellsCount = field.length * field[0].length;
            }

            return openedCells;
        }
    }

    /**
     * BFS algorithm opening the current closed non-mined cell and all closed neighbouring
     * cells according to minesweeper rules.
     */
    private List<Cell> openEmptyCell(Cell cell) {
        List<Cell> openedCells = new ArrayList<>();
        Queue<Cell> queue = new ArrayDeque<>();
        queue.add(cell);
        cell.open = true;

        while (!queue.isEmpty()) {
            cell = queue.remove();
            this.openCellsCount++;
            openedCells.add(cell);
            int minesAround = Character.digit(cell.value, 10);
            if (minesAround == 0) {
                List<Cell> neighbours = getNeighbours(cell).stream()
                        .filter(c -> !c.open).collect(Collectors.toList());
                for (Cell neighbour : neighbours) {
                    queue.add(neighbour);
                    neighbour.open = true;
                }
            }
        }

        return openedCells;
    }

    public State getState() {
        return state;
    }

    public Cell[][] getField() {
        return field;
    }

    public int getMinesCount() {
        return minesCount;
    }

    public int getHeight() {
        return field.length;
    }

    public int getWidth() {
        return field[0].length;
    }

    private int countNeighboringMines(Cell cell) {
        return (int) getNeighbours(cell).stream().filter(n -> n.value == 'X').count();
    }

    private List<Cell> getNeighbours(Cell cell) {
        List<Cell> neighbours = new ArrayList<>();
        for (int i = cell.row - 1; i <= cell.row + 1; i++) {
            for (int j = cell.column - 1; j <= cell.column + 1; j++) {
                if ((i != cell.row || j != cell.column)
                        && i >= 0 && i < field.length && j >= 0 && j < field[0].length) {
                    neighbours.add(field[i][j]);
                }
            }
        }

        return  neighbours;
    }
}
