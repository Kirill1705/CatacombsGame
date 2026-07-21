package ru.vikhrenko.catacombsGame.core.port.output;

import java.util.Optional;
import java.util.UUID;

public interface TaskRepository {
    void addTask(UUID playerId, int taskId);

    Optional<Integer> getTask(UUID playerId);
}
