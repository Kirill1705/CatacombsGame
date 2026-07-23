package ru.vikhrenko.catacombsGame.core.port.output;

import java.time.Duration;
import java.util.function.Consumer;
import java.util.function.Function;

public interface TimerProvider {
    int addTaskRepeating(Consumer<Duration> task, Runnable afterTask, Function<Exception, Boolean> onException, Duration period, Duration duration);

    int addDelayedTask(Runnable task, Duration delay);

    void cancelTask(int taskId);
}
