package ru.vikhrenko.catacombsGame.infrastructure;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.structure.Structure;
import org.example.minigame_service.core.port.dto.PositionDto;
import ru.vikhrenko.catacombsGame.core.port.output.StructuresManager;
import thor.core.port.input.MapEngineService;
import thor.usefulUtils.utils.StructureUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
public class StructuresManagerImpl implements StructuresManager {
    private final StructuresManagerConfig config;
    private final String worldName;
    private final MapEngineService mapEngineService;

    private final Set<PositionDto> lobbies = new HashSet<>();

    @Override
    public void placeLobbyIfNotExists(PositionDto minigameLocation) {
        if (!lobbies.contains(minigameLocation)) {
            try {
                Structure structure = Bukkit.getStructureManager().loadStructure(config.getLobbyPath().toFile());
                StructureUtils.place(structure, new Location(Bukkit.getWorld(worldName), minigameLocation.x(), minigameLocation.y(), minigameLocation.z()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void tptoRandomPlaceOnArena(UUID playerId, UUID mapId) {
        mapEngineService.tptoArena(playerId, mapId);
    }
}
