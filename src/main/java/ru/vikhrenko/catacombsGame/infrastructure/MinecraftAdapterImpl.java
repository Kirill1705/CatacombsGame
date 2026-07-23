package ru.vikhrenko.catacombsGame.infrastructure;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.example.minigame_service.core.port.dto.ScoreboardData;
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
    private final NamespacedKey deadKey;

    @Override
    public void teleport(PositionDto locationDto, UUID playerId) {
        Player player = Bukkit.getPlayer(playerId);
        if (player == null) return;
        player.teleport(new Location(Bukkit.getWorld(worldName), locationDto.x(), locationDto.y(), locationDto.z()));
    }

    @Override
    public void titleGameResult(UUID playerId, int place) {
        Player player = Bukkit.getPlayer(playerId);
        if (player == null) return;
        player.sendTitlePart(TitlePart.TITLE, Component.text("Ваше место: " + place));
    }

    @Override
    public void drawScoreboard(UUID playerId, Duration remainTime, List<UUID> players) {
        List<ScoreboardData> data = players.stream()
                .map(uuid -> new ScoreboardData(minecraftUtils.getPlayerName(uuid), 1))
                .toList();
        Component name = Component.text("Catacombs. Tp to arena in " + remainTime.toMinutes() + ":" + (remainTime.getSeconds() % 60));
        minecraftUtils.drawScoreboard(playerId, name, data);
    }

    @Override
    public void setNightVision(UUID playerId) {
        Player player = Bukkit.getPlayer(playerId);
        if (player == null) return;
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1));
    }

    @Override
    public boolean isDead(UUID playerId) {
        Player player = Bukkit.getPlayer(playerId);
        if (player == null) return true;
        return player.isDead();
    }

    @Override
    public void makeObserverAndSetSpawnPoint(UUID playerId) {
        Player player = Bukkit.getPlayer(playerId);
        if (player == null) return;
        Location location = player.getLocation();
        player.setRespawnLocation(location, true);
        PersistentDataContainer container = player.getPersistentDataContainer();
        container.set(deadKey, PersistentDataType.BOOLEAN, true);
    }
}
