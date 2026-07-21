package ru.vikhrenko.catacombsGame;

import lombok.extern.slf4j.Slf4j;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.example.minigame_service.core.port.input.LobbyService;
import org.example.minigame_service.core.port.input.MinigameService;
import org.example.minigame_service.infrastructure.MinecraftUtilsImpl;
import org.example.minigame_service.infrastructure.TaskStorageImpl;
import org.example.minigame_service.infrastructure.TimerProviderImpl;
import ru.vikhrenko.catacombsGame.core.port.input.CatacombsService;
import ru.vikhrenko.catacombsGame.core.service.CatacombsServiceImpl;
import ru.vikhrenko.catacombsGame.infrastructure.*;
import ru.vikhrenko.catacombsGame.infrastructure.repositories.TaskRepositoryAdapter;
import ru.vikhrenko.catacombsGame.presentation.CatacombsEventListener;
import thor.core.port.input.MapEngineService;
import thor.core.port.input.MapService;
import thor.usefulUtils.reload.CommandManager;

import java.util.List;

@Slf4j
public final class CatacombsGame extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        uploadResources();

        FileConfiguration configuration = getConfig();
        String worldName = configuration.getString("world", "catacombs");
        createWorldIfNotExists(worldName);

        MinigameService minigameService = Bukkit.getServicesManager().getRegistration(MinigameService.class).getProvider();
        LobbyService lobbyService = Bukkit.getServicesManager().getRegistration(LobbyService.class).getProvider();
        MapService mapService = Bukkit.getServicesManager().getRegistration(MapService.class).getProvider();
        MapEngineService mapEngineService = Bukkit.getServicesManager().getRegistration(MapEngineService.class).getProvider();

        StructuresManagerConfig config = new StructuresManagerConfig();
        new CommandManager().registerReloadCommand(this, List.of(config));

        CatacombsService service = new CatacombsServiceImpl(
            new ServiceAdapterImpl(minigameService, lobbyService, mapEngineService, worldName),
            new MinecraftAdapterImpl(new MinecraftUtilsImpl(), worldName),
            new StructuresManagerImpl(config, worldName, mapEngineService),
            new TimerProviderAdapter(new TimerProviderImpl(this)),
            new ConcurrentMapCreator(mapService),
            new TaskRepositoryAdapter(new TaskStorageImpl())
        );

        CatacombsEventListener listener = new CatacombsEventListener(service);
        getServer().getPluginManager().registerEvents(listener, this);
    }

    private void uploadResources() {
        saveResource("lobby.nbt", false);
    }

    @Override
    public void onDisable() {

    }

    private void createWorldIfNotExists(String worldName) {
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            log.info("Creating world {}", worldName);
            WorldCreator creator = new WorldCreator(worldName);
            creator.generateStructures(false);
            creator.type(WorldType.FLAT);
            creator.generatorSettings("{\"layers\":[{\"block\":\"minecraft:air\",\"height\":1}],\"biome\":\"minecraft:the_void\"}");
            world = creator.createWorld();
        }
        world.setDifficulty(Difficulty.HARD);
        world.setGameRule(GameRules.SPAWN_MOBS, false);
        world.setGameRule(GameRules.ADVANCE_WEATHER, false);
        world.setGameRule(GameRules.ADVANCE_TIME, false);
    }
}
