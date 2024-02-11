package com.example.minesweeper.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cells")
@Getter
@Setter
public class Cell {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @JoinColumn(name = "game_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private MinesweeperGame game;

    @Column(name = "\"row\"", nullable = false)
    private Integer row;

    @Column(name = "\"column\"", nullable = false)
    private Integer column;

    @Column(name = "\"value\"", nullable = false)
    private Character value;

    @Column(name = "\"open\"", nullable = false)
    private Boolean open;
}
