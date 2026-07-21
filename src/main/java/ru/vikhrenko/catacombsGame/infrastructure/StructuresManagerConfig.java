package ru.vikhrenko.catacombsGame.infrastructure;

import lombok.Data;
import thor.usefulUtils.reload.YamlAbstractReloadable;

import java.nio.file.Path;

public class StructuresManagerConfig extends YamlAbstractReloadable<StructuresManagerConfig.Options> {

    public StructuresManagerConfig() {
        super(new Options(), Options.class);
    }

    public Path getLobbyPath() {
        return Path.of(getOptions().lobbyPath);
    }

    @Data
    public static class Options {
        private String lobbyPath = "lobby.nbt";
    }
}
