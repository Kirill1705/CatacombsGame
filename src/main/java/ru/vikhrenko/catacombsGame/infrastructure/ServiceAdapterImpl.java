package ru.vikhrenko.catacombsGame.infrastructure;

import lombok.RequiredArgsConstructor;
import org.example.minigame_service.core.port.dto.MinigameDto;
import org.example.minigame_service.core.port.input.LobbyService;
import org.example.minigame_service.core.port.input.MinigameService;
import ru.vikhrenko.catacombsGame.core.port.output.ServiceAdapter;
import thor.core.port.input.MapEngineService;
import thor.core.port.mapping.MapPlaceOptions;
import thor.core.port.mapping.dto.map.PlacedMapDto;
import ru.vikhrenko.serverUtils.utils.dataStructures.Point;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class ServiceAdapterImpl implements ServiceAdapter {
    private final MinigameService minigameService;
    private final LobbyService lobbyService;
    private final MapEngineService mapService;
    private final String worldName;

    @Override
    public PlacedMapDto placeMap(Point location, UUID mapId) {
        MapPlaceOptions options = new MapPlaceOptions();
        return mapService.placeMap(location, worldName, mapId, options);
    }

    @Override
    public boolean removePlayerFromMinigame(UUID playerId) {
        return minigameService.removePlayerFromMinigame(playerId, GameConstants.NAME);
    }

    @Override
    public void wipe(UUID playerId) {
        lobbyService.wipeToLobby(playerId);
    }

    @Override
    public void addPlayerToMinigame(UUID playerId) {
        minigameService.addPlayerToMinigame(playerId, GameConstants.NAME);
    }

    @Override
    public boolean finish(UUID minigameId) {
        return minigameService.finish(minigameId);
    }

    @Override
    public Optional<MinigameDto> getPlayerMinigame(UUID playerId) {
        return minigameService.getPlayerMinigame(playerId, GameConstants.NAME);
    }
}
