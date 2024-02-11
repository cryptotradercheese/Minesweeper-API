package com.example.minesweeper.model;

import com.example.minesweeper.service.State;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "minesweeper_games")
@Getter
@Setter
public class MinesweeperGame {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "height", nullable = false)
    private Integer height;

    @Column(name = "width", nullable = false)
    private Integer width;

    @Column(name = "mines_count", nullable = false)
    private Integer minesCount;

    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private State state;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "game", fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.PERSIST})
    private List<Cell> cells;

    @Transient
    private Cell[][] field;

    public Cell[][] getField() {
        if (field == null) {
            field = new Cell[height][width];
            for (Cell cell : cells) {
                field[cell.getRow()][cell.getColumn()] = cell;
            }
        }

        return field;
    }

    public void setField(Cell[][] field) {
        Objects.requireNonNull(field);
        List<Cell> cells = new ArrayList<>();
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {
                Cell cell = field[i][j];
                cell.setGame(this);
                cells.add(cell);
            }
        }

        this.cells = cells;
        this.field = field;
    }

    public void updateField(Cell[][] field) {
        Objects.requireNonNull(field);
        Objects.requireNonNull(cells);
        for (Cell cell : cells) {
            Cell fieldCell = field[cell.getRow()][cell.getColumn()];
            cell.setValue(fieldCell.getValue());
            cell.setOpen(fieldCell.getOpen());
        }
    }
}
