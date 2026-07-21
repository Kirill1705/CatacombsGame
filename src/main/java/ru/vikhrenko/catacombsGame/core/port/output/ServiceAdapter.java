package ru.vikhrenko.catacombsGame.core.port.output;

import org.example.minigame_service.core.port.dto.MinigameDto;
import org.example.minigame_service.core.port.dto.PositionDto;
import thor.core.port.mapping.dto.map.PlacedMapDto;

import java.util.Optional;
import java.util.UUID;

public interface ServiceAdapter {
    PlacedMapDto placeMap(PositionDto location, UUID mapId);

    boolean removePlayerFromMinigame(UUID playerId);

    void wipe(UUID playerId);

    void addPlayerToMinigame(UUID playerId);

    boolean finish(UUID playerId);

    Optional<MinigameDto> getPlayerMinigame(UUID playerId);
}
