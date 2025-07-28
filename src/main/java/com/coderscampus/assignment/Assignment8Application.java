package com.coderscampus.assignment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class Assignment8Application {
    public static void main(String[] args) {
        // Step 1 Asynchronous data fetching
        // The solution to synchronous programming is Multi-threading
        // Thread pool-ExecutorService to manage threads-Concurrent Calls
        // Thread-safe counting-AtomicInteger will track frequencies
        // 1 initialize thread pool - ExecutorService - executors
        // 2 submit tasks - List - ArrayLists - futures
        // 3 wait for completion - for loop

        // Step 2 - count number of frequencies - count iterations - output
        // 1 Thread-safe counter - AtomicInteger - into Array - for loop
        // 2 Process each task - for loop
        // 3 Print results - Syso-result

        Assignment8 assignment = new Assignment8();

        // Initialize counters for numbers 0-14
        AtomicInteger[] counts = new AtomicInteger[15];
        for (int i = 0; i < 15; i++) {
            counts[i] = new AtomicInteger(0);
        }

        // Thread pool with 200 threads at a time (Concurrently)
        ExecutorService executor = Executors.newFixedThreadPool(200);

        // submit all 1,000 tasks
        List<Future<?>> futures = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            futures.add(executor.submit(() -> {
                List<Integer> tasks = assignment.getNumbers();
                for (Integer number : tasks) {
                    if (number >= 0 && number <= 14) {
                        counts[number].incrementAndGet(); // keeps breaking here
                    }
                }
            }));
        }

        // Wait for tasks to complete
        for (Future<?> future : futures) {
            try {
                future.get(); // breaks here
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();

        // Build and print results
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 15; i++) {
            result.append(i).append("=").append(counts[i].get());
            if (i < 15) result.append(", ");
        }
        System.out.println(result);

        int totalCount = 0;
        for (int i = 0; i < 15; i++) {
            totalCount += counts[i].get();
        }
        System.out.println("Total numbers processed: " + totalCount);
    }
}