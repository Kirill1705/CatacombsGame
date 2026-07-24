package ru.vikhrenko.catacombsGame.infrastructure;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.structure.Structure;
import ru.vikhrenko.catacombsGame.core.port.output.StructuresManager;
import thor.core.port.input.MapEngineService;
import thor.usefulUtils.exception.EntityNotFoundException;
import thor.usefulUtils.utils.StructureUtils;
import thor.usefulUtils.utils.dataStructures.Point;
import thor.usefulUtils.utils.dataStructures.Points;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

@RequiredArgsConstructor
public class StructuresManagerImpl implements StructuresManager {
    private final StructuresManagerConfig config;
    private final String worldName;
    private final MapEngineService mapEngineService;
    private final Path dataPath;

    private final Map<Point, Point> lobbies = new HashMap<>();

    @Override
    public void placeLobbyIfNotExists(Point minigameLocation) {
        if (!lobbies.containsKey(minigameLocation)) {
            try {
                Structure structure = Bukkit.getStructureManager().loadStructure(dataPath.resolve(config.getLobbyPath()).toFile());
                Point lobbyPosition = new Point(minigameLocation.x(), minigameLocation.y(), minigameLocation.z()).subtract(Points.fromVector(structure.getSize()));
                StructureUtils.place(structure, lobbyPosition.toLocation(Bukkit.getWorld(worldName)));
                lobbies.put(minigameLocation, Points.fromVector(structure.getSize()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void tptoLobby(Point minigameLocation, UUID playerId) {
        if (!lobbies.containsKey(minigameLocation)) {
            throw new EntityNotFoundException(Point.class);
        }
        Point size = lobbies.get(minigameLocation);
        Point tpPoint = new Point(minigameLocation.x(), minigameLocation.y(), minigameLocation.z()).subtract(size).add(config.getLobbySpawn());
        Player player = Bukkit.getPlayer(playerId);
        if (player == null) {
            throw new EntityNotFoundException(Player.class);
        }
        player.teleport(tpPoint.toLocation(Bukkit.getWorld(worldName)));
    }

    @Override
    public void tptoRandomPlaceOnArena(UUID playerId, UUID mapId) {
        mapEngineService.tptoArena(playerId, mapId);
    }
}
