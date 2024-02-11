package com.example.minesweeper.repository;

import com.example.minesweeper.model.MinesweeperGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MinesweeperGameRepository extends JpaRepository<MinesweeperGame, String> {
}
