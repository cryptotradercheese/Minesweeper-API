package com.example.minesweeper.service;

public class Cell {
    public final int row;
    public final int column;
    public char value;
    public boolean open;

    public Cell(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public Cell(int row, int column, char value, boolean open) {
        this(row, column);
        this.value = value;
        this.open = open;
    }
}
