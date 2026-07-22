package ru.vikhrenko.catacombsGame.infrastructure;

import lombok.Data;
import thor.usefulUtils.reload.YamlAbstractReloadable;
import thor.usefulUtils.utils.dataStructures.BlockPosition;
import thor.usefulUtils.utils.dataStructures.Point;

import java.nio.file.Path;

public class StructuresManagerConfig extends YamlAbstractReloadable<StructuresManagerConfig.Options> {

    public StructuresManagerConfig() {
        super(new Options(), Options.class);
    }

    public Path getLobbyPath() {
        return Path.of(getOptions().lobbyPath);
    }

    public BlockPosition getLobbySpawn() {
        return new Point(getOptions().lobbySpawn[0], getOptions().lobbySpawn[1], getOptions().lobbySpawn[2]);
    }

    @Data
    public static class Options {
        private String lobbyPath = "lobby.nbt";

        private Integer[] lobbySpawn = {1, 1, 1};
    }
}
