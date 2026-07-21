package ru.vikhrenko.catacombsGame.infrastructure;

import lombok.RequiredArgsConstructor;
import ru.vikhrenko.catacombsGame.core.port.output.TimerProvider;

import java.time.Duration;
import java.util.function.Consumer;
import java.util.function.Function;

@RequiredArgsConstructor
public class TimerProviderAdapter implements TimerProvider {
    private final org.example.minigame_service.core.port.output.TimerProvider timerProvider;
    @Override
    public int addTaskRepeating(Consumer<Duration> task, Runnable afterTask, Function<Exception, Boolean> onException, Duration period, Duration duration) {
        return timerProvider.addTask(task, afterTask, onException, period, duration);
    }

    @Override
    public int addDelayedTask(Runnable task, Duration delay) {
        return timerProvider.addDelayedTask(task, delay);
    }

    @Override
    public void cancelTask(int taskId) {
        timerProvider.cancelTask(taskId);
    }
}
