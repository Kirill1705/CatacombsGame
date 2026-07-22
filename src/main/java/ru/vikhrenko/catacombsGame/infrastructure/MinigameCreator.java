package ru.vikhrenko.catacombsGame.infrastructure;

import org.example.minigame_service.core.minigame.Minigame;
import org.example.minigame_service.core.minigame.MinigameInfo;
import org.example.minigame_service.core.port.dto.MinigameInfoDto;

import java.time.Duration;

public class MinigameCreator {
    public MinigameInfoDto create(String worldName) {
        return new MinigameInfoDto(
                worldName,
                GameConstants.NAME,
                1000,
                10,
                Duration.ofSeconds(30),
                Duration.ofSeconds(10),
                2
        );
    }
}
