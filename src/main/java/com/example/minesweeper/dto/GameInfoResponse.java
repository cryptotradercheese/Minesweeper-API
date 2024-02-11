package com.example.minesweeper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameInfoResponse {
    @JsonProperty("game_id")
    private String gameId;

    @JsonProperty("height")
    private int height;

    @JsonProperty("width")
    private int width;

    @JsonProperty("mines_count")
    private int minesCount;

    @JsonProperty("completed")
    private boolean completed;

    @JsonProperty("field")
    private char[][] field;

    public String[][] getField() {
        String[][] field = new String[this.field.length][this.field[0].length];
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {
                field[i][j] = String.valueOf(this.field[i][j]);
            }
        }
        return field;
    }
}
