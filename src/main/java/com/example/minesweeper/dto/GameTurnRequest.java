package com.example.minesweeper.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class GameTurnRequest {
    @NotNull
    private final String gameId;

    @Min(0)
    private final int column;

    @Min(0)
    private final int row;

    @JsonCreator
    public GameTurnRequest(@JsonProperty(value = "game_id", required = true) String gameId,
                           @JsonProperty(value = "col", required = true) int column,
                           @JsonProperty(value = "row", required = true) int row) {
        this.gameId = gameId;
        this.column = column;
        this.row = row;
    }
}
