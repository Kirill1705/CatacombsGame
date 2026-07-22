package ru.vikhrenko.catacombsGame.infrastructure;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.structure.Structure;
import org.example.minigame_service.core.port.dto.PositionDto;
import ru.vikhrenko.catacombsGame.core.port.output.StructuresManager;
import thor.core.port.input.MapEngineService;
import thor.usefulUtils.exception.EntityNotFoundException;
import thor.usefulUtils.utils.StructureUtils;
import thor.usefulUtils.utils.dataStructures.BlockLocation;
import thor.usefulUtils.utils.dataStructures.BlockPosition;
import thor.usefulUtils.utils.dataStructures.Point;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

@RequiredArgsConstructor
public class StructuresManagerImpl implements StructuresManager {
    private final StructuresManagerConfig config;
    private final String worldName;
    private final MapEngineService mapEngineService;
    private final Path dataPath;

    private final Map<PositionDto, BlockPosition> lobbies = new HashMap<>();

    @Override
    public void placeLobbyIfNotExists(PositionDto minigameLocation) {
        if (!lobbies.containsKey(minigameLocation)) {
            try {
                Structure structure = Bukkit.getStructureManager().loadStructure(dataPath.resolve(config.getLobbyPath()).toFile());
                BlockPosition lobbyPosition = new Point(minigameLocation.x(), minigameLocation.y(), minigameLocation.z()).subtract(new Point(structure.getSize()));
                StructureUtils.place(structure, new BlockLocation(lobbyPosition, Bukkit.getWorld(worldName)).toLocation());
                lobbies.put(minigameLocation, new Point(structure.getSize()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void tptoLobby(PositionDto minigameLocation, UUID playerId) {
        if (!lobbies.containsKey(minigameLocation)) {
            throw new EntityNotFoundException(PositionDto.class);
        }
        BlockPosition size = lobbies.get(minigameLocation);
        BlockPosition tpPoint = new Point(minigameLocation.x(), minigameLocation.y(), minigameLocation.z()).subtract(size).add(config.getLobbySpawn());
        Player player = Bukkit.getPlayer(playerId);
        if (player == null) {
            throw new EntityNotFoundException(Player.class);
        }
        player.teleport(new BlockLocation(tpPoint, Bukkit.getWorld(worldName)).toLocation());
    }

    @Override
    public void tptoRandomPlaceOnArena(UUID playerId, UUID mapId) {
        mapEngineService.tptoArena(playerId, mapId);
    }
}
