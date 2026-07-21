package ru.vikhrenko.catacombsGame.infrastructure;

import lombok.extern.slf4j.Slf4j;
import ru.vikhrenko.catacombsGame.core.port.output.MapCreator;
import thor.core.port.input.MapService;

import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class ConcurrentMapCreator implements MapCreator {
    private static final int MAX_GENERATED_MAPS = 5;

    private final MapService mapService;

    private final BlockingQueue<UUID> maps;

    private final ExecutorService executorService;
    private final AtomicBoolean isRunning;

    public ConcurrentMapCreator(MapService mapService) {
        this.mapService = mapService;
        this.maps = new LinkedBlockingDeque<>(MAX_GENERATED_MAPS);
        this.isRunning = new AtomicBoolean(true);
        this.executorService = Executors.newFixedThreadPool(MAX_GENERATED_MAPS, runnable -> {
            Thread thread = new Thread(runnable, "Catacombs-Map-Generator");
            thread.setDaemon(true);
            return thread;
        });

        for (int i = 0; i < MAX_GENERATED_MAPS; i++) {
            startThread();
        }
    }

    @Override
    public UUID createNewMap() {
        UUID map = maps.poll();
        if (map != null) {
            log.info("Map taken from buffer. Remain buffer size: {}", maps.size());
            return map;
        }
        log.warn("Buffer is empty. Using blocking method");
        try {
            return maps.take();
        } catch (InterruptedException e) {
            log.warn("Generating map in main server thread!");
            return mapService.generateMap();
        }
    }

    private void startThread() {
        executorService.submit(() -> {
            while (isRunning.get() && !Thread.currentThread().isInterrupted()) {
                try {
                    log.debug("[{}] Starting generate map", Thread.currentThread().getName());
                    UUID generatedMap = mapService.generateMap();
                    maps.put(generatedMap);
                    log.debug("[{}] Map added to buffer. Buffer size: {}", Thread.currentThread().getName(), maps.size());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.info("[{}] Map generator thread was interrupted", Thread.currentThread().getName());
                } catch (Exception e) {
                    log.error("Ошибка в параллельном воркере: {}", e.getMessage(), e);
                    try { TimeUnit.SECONDS.sleep(5); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
                }
            }
        });
    }
}
