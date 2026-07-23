package ru.vikhrenko.catacombsGame.infrastructure.repositories;

import lombok.RequiredArgsConstructor;
import org.example.minigame_service.core.port.output.TaskStorage;
import ru.vikhrenko.catacombsGame.core.port.output.TaskRepository;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class TaskRepositoryAdapter implements TaskRepository {
    private final TaskStorage taskStorage;

    @Override
    public void addTask(UUID playerId, int taskId) {
        taskStorage.addTask(playerId, taskId);
    }

    @Override
    public Optional<Integer> getTask(UUID playerId) {
        return taskStorage.getTask(playerId);
    }
}
