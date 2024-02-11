package com.example.minesweeper.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class NewGameRequest {
    @Min(2)
    @Max(30)
    private final int width;

    @Min(2)
    @Max(30)
    private final int height;

    @Min(1)
    private final int minesCount;

    @JsonCreator
    public NewGameRequest(@JsonProperty(value = "width", required = true) int width,
                          @JsonProperty(value = "height", required = true) int height,
                          @JsonProperty(value = "mines_count", required = true) int minesCount) {
        if (minesCount >= width * height) {
            throw new IllegalArgumentException("mines_count must be no more than width * height - 1");
        }

        this.width = width;
        this.height = height;
        this.minesCount = minesCount;
    }
}
