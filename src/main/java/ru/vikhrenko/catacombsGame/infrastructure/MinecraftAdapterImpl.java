package ru.vikhrenko.catacombsGame.infrastructure;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.example.minigame_service.core.port.output.MinecraftUtils;
import ru.vikhrenko.catacombsGame.core.port.output.MinecraftAdapter;
import thor.core.port.mapping.dto.PositionDto;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class MinecraftAdapterImpl implements MinecraftAdapter {
    private final MinecraftUtils minecraftUtils;

    private final String worldName;

    @Override
    public void teleport(PositionDto locationDto, UUID playerId) {
        Player player = Bukkit.getPlayer(playerId);
        player.teleport(new Location(Bukkit.getWorld(worldName), locationDto.x(), locationDto.y(), locationDto.z()));
    }

    @Override
    public void titleGameResult(UUID playerId, int place) {
        Player player = Bukkit.getPlayer(playerId);
        player.sendTitlePart(TitlePart.TITLE, Component.text("Ваше место: " + place));
    }

    @Override
    public void drawScoreboard(UUID playerId, Duration remainTime, List<UUID> players) {
        minecraftUtils.drawScoreboard(playerId, GameConstants.NAME, remainTime, players);
    }
}
