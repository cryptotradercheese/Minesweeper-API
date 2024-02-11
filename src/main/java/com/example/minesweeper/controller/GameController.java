package com.example.minesweeper.controller;

import com.example.minesweeper.dto.GameInfoResponse;
import com.example.minesweeper.dto.GameTurnRequest;
import com.example.minesweeper.dto.NewGameRequest;
import com.example.minesweeper.service.GameService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api")
public class GameController {
    private GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/new")
    public ResponseEntity<GameInfoResponse> newGame(@RequestBody @Valid NewGameRequest request) {
        GameInfoResponse body = gameService
                .createGame(request.getHeight(), request.getWidth(), request.getMinesCount());
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @PostMapping("/turn")
    public ResponseEntity<GameInfoResponse> turn(@RequestBody @Valid GameTurnRequest request) {
        GameInfoResponse body = gameService
                .turn(request.getGameId(), request.getRow(), request.getColumn());
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }
}
