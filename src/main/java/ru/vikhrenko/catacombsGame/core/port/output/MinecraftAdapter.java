package ru.vikhrenko.catacombsGame.core.port.output;

import thor.usefulUtils.utils.dataStructures.Point;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

public interface MinecraftAdapter {
    void teleport(Point position, UUID playerId);

    void titleGameResult(UUID playerId, int place);

    void drawScoreboard(UUID playerId, Duration remainTime, List<UUID> players);

    void setNightVision(UUID playerId);

    boolean isDead(UUID playerId);

    void makeObserverAndSetSpawnPoint(UUID playerId);
}
