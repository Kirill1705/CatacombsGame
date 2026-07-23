package ru.vikhrenko.catacombsGame.core.port.input;

import org.example.minigame_service.core.port.dto.MinigameDto;

import java.util.UUID;

public interface CatacombsService {
    void joinPlayerEvent(MinigameDto minigameDto, UUID playerId);

    void addPlayerToGame(UUID playerId);

    void onDeath(UUID playerId);

    void onRemoveEvent(MinigameDto minigame, UUID playerId);

    void startGame(MinigameDto minigame);
}
