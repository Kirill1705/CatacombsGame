package ru.vikhrenko.catacombsGame.core.port.output;

import thor.usefulUtils.utils.dataStructures.Point;

import java.util.UUID;

public interface StructuresManager {
    void placeLobbyIfNotExists(Point minigameLocation);

    void tptoLobby(Point minigameLocation, UUID playerId);

    void tptoRandomPlaceOnArena(UUID playerId, UUID mapId);
}
