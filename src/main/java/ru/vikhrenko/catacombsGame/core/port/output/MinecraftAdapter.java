package ru.vikhrenko.catacombsGame.core.port.output;

import thor.core.port.mapping.dto.PositionDto;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

public interface MinecraftAdapter {
    void teleport(PositionDto locationDto, UUID playerId);

    void titleGameResult(UUID playerId, int place);

    void drawScoreboard(UUID playerId, Duration remainTime, List<UUID> players);
}
