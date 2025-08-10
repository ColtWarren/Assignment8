package com.coderscampus.assignment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

class Assignment8Service {

    private final Assignment8 assignment;
    private final ConcurrentHashMap<Integer, AtomicInteger> counts;
    private final ExecutorService executor;

    public Assignment8Service() {
        this.assignment = new Assignment8();
        this.counts = new ConcurrentHashMap<>();
        this.executor = Executors.newFixedThreadPool(200);
    }

    public void performConcurrentCounting(int numberOfTasks) {
        List<Future<?>> futures = new ArrayList<>();

        for (int i = 0; i < numberOfTasks; i++) {
            futures.add(executor.submit(() -> {
                List<Integer> numbers = assignment.getNumbers();
                numbers.forEach(n ->
                        counts.computeIfAbsent(n, key -> new AtomicInteger(0))
                              .incrementAndGet()
                );
            }));
        }

        waitForTaskCompletion(futures);
    }

    private void waitForTaskCompletion(List<Future<?>> futures) {
        futures.forEach(future -> {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                System.err.println("Error in task: " + e.getMessage());
            }
        });
    }

    public void printResults() {

        counts.entrySet().stream()
                .sorted((entry1, entry2) -> entry1.getKey().compareTo(entry2.getKey()))
                .forEach(entry ->
                        System.out.println(entry.getKey() + "=" + entry.getValue().get())
                );

        printSummaryStatistics();
    }

    private void printSummaryStatistics() {
        int totalCount = counts.values().stream()
                .mapToInt(AtomicInteger::get)
                .sum();
        System.out.println("Total numbers processed: " + totalCount);
    }

    public void shutdown() {
        executor.shutdown();
    }
}

