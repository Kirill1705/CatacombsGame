package ru.vikhrenko.catacombsGame.presentation;

import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.example.minigame_service.infrastructure.event.JoinMinigameEvent;
import org.example.minigame_service.infrastructure.event.MinigameStartEvent;
import org.example.minigame_service.infrastructure.event.PlayerLeaveMinigameEvent;
import ru.vikhrenko.catacombsGame.core.port.input.CatacombsService;

@RequiredArgsConstructor
public class CatacombsEventListener implements Listener {
    private final CatacombsService service;

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
}
