package ru.vikhrenko.catacombsGame.presentation;

import lombok.RequiredArgsConstructor;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.example.minigame_service.infrastructure.event.JoinMinigameEvent;
import org.example.minigame_service.infrastructure.event.MinigameStartEvent;
import org.example.minigame_service.infrastructure.event.PlayerLeaveMinigameEvent;
import ru.vikhrenko.catacombsGame.core.port.input.CatacombsService;

@RequiredArgsConstructor
public class CatacombsEventListener implements Listener {
    private final CatacombsService service;

    private final NamespacedKey deadKey;

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        service.onDeath(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onJoinMinigame(JoinMinigameEvent event) {
        service.joinPlayerEvent(event.getMinigameDto(), event.getPlayerId());
    }

    @EventHandler
    public void onStart(MinigameStartEvent event) {
        service.startGame(event.getMinigameDto());
    }

    @EventHandler
    public void onRemove(PlayerLeaveMinigameEvent event) {
        service.onRemoveEvent(event.getMinigameDto(), event.getPlayerId());
    }

    @EventHandler
    public void onExit(PlayerQuitEvent event) {
        service.onDeath(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        PersistentDataContainer container = event.getPlayer().getPersistentDataContainer();
        if (container.has(deadKey)) {
            event.getPlayer().setGameMode(GameMode.SPECTATOR);
            container.remove(deadKey);
        }
    }
}
