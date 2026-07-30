package ru.vikhrenko.catacombsGame.core.port.output;

import ru.vikhrenko.serverUtils.utils.dataStructures.Point;

import java.util.UUID;

public interface StructuresManager {
    void placeLobbyIfNotExists(Point minigameLocation);

    void tptoLobby(Point minigameLocation, UUID playerId);

    void tptoRandomPlaceOnArena(UUID playerId, UUID mapId);
}
