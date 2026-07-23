package ru.vikhrenko.catacombsGame.core.port.output;

import org.example.minigame_service.core.port.dto.PositionDto;

import java.util.UUID;

public interface StructuresManager {
    void placeLobbyIfNotExists(PositionDto minigameLocation);

    void tptoLobby(PositionDto minigameLocation, UUID playerId);

    void tptoRandomPlaceOnArena(UUID playerId, UUID mapId);
}
