package ru.vikhrenko.catacombsGame.core.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.minigame_service.core.port.dto.MinigameDto;
import ru.vikhrenko.catacombsGame.core.port.input.CatacombsService;
import ru.vikhrenko.catacombsGame.core.port.output.*;
import thor.core.port.mapping.dto.PositionDto;
import thor.core.port.mapping.dto.map.PlacedMapDto;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
public class CatacombsServiceImpl implements CatacombsService {
    private final ServiceAdapter serviceAdapter;
    private final MinecraftAdapter minecraftAdapter;
    private final StructuresManager structuresManager;
    private final TimerProvider timerProvider;
    private final MapCreator mapCreator;
    private final TaskRepository taskRepository;

    @Override
    public void joinPlayerEvent(MinigameDto minigameDto, UUID playerId) {
        structuresManager.placeLobbyIfNotExists(minigameDto.positionDto());
        var position = minigameDto.positionDto();
        minecraftAdapter.teleport(new PositionDto(position.x(), position.y(), position.z()), playerId);
    }

    @Override
    public void addPlayerToGame(UUID playerId) {
        serviceAdapter.addPlayerToMinigame(playerId);
    }

    @Override
    public void onDeath(UUID playerId) {
        serviceAdapter.removePlayerFromMinigame(playerId);
    }

    @Override
    public void onRemoveEvent(MinigameDto minigame, UUID playerId) {
        removeTask(playerId);
        serviceAdapter.wipe(playerId);
        if (minigame.playerIds().size() == 1) {
            win(minigame.playerIds().getFirst());
        }
        if (!minigame.playerIds().isEmpty()) {
            minecraftAdapter.titleGameResult(playerId, minigame.playerIds().size() + 1);
        }
    }

    @Override
    public void startGame(MinigameDto minigame) {
        UUID mapId = mapCreator.createNewMap();
        PlacedMapDto mapDto = serviceAdapter.placeMap(minigame.positionDto(), mapId);
        tpPlayers(mapDto, minigame.playerIds());
        startTimer(minigame, mapDto.id());
    }

    private void startTimer(MinigameDto minigame, UUID placedMapId) {
        final int gameDuration = 40;
        for (UUID playerId: minigame.playerIds()) {
            removeTask(playerId);
            int playerTask = timerProvider.addTaskRepeating(
                    (duration) -> {
                        minecraftAdapter.drawScoreboard(playerId, duration, minigame.playerIds());
                    },
                    () -> {},
                    (Exception e) -> {
                        log.error("Exception in timer task {}", e.getMessage());
                        return false;
                    },
                    Duration.ofSeconds(1),
                    Duration.ofMinutes(gameDuration)
            );
            taskRepository.addTask(playerId, playerTask);
        }
        removeTask(minigame.id());
        int task = timerProvider.addDelayedTask(() -> {
            for (UUID playerId: minigame.playerIds()) {
                removeTask(playerId);
                structuresManager.tptoRandomPlaceOnArena(playerId, placedMapId);
            }
        }, Duration.ofMinutes(gameDuration));
        taskRepository.addTask(minigame.id(), task);
    }

    private void removeTask(UUID playerId) {
        Optional<Integer> task = taskRepository.getTask(playerId);
        task.ifPresent(timerProvider::cancelTask);
    }

    private void tpPlayers(PlacedMapDto mapDto, List<UUID> playerIds) {
        for (int i = 0; i < playerIds.size(); i++) {
            minecraftAdapter.teleport(mapDto.playerSpawnPlaces().get(i).positionDto(), playerIds.get(i));
        }
    }

    private void win(UUID winnerId) {
        minecraftAdapter.titleGameResult(winnerId, 1);
        timerProvider.addDelayedTask(
                () -> serviceAdapter.removePlayerFromMinigame(winnerId),
                Duration.ofSeconds(10)
        );
    }
}
