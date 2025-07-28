package com.coderscampus.assignment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Assignment8Application {
    public static void main(String[] args) {
        // Creates object
        Assignment8 assignment = new Assignment8();
        // Creates a new AtomicInteger instance - counters
        // Creates a range of Integers - maps each integer
        // Collects Integers into an Array (0 - 14)
        AtomicInteger[] counts = IntStream.range(0, 15)                                     // Step 1: Create range
                                          .mapToObj(i -> new AtomicInteger(0))    // Step 2: Map to objects
                                          .toArray(AtomicInteger[]::new);                   // Step 3: Convert to array

        // Thread pool with 200 threads at a time (Concurrently)
        ExecutorService executor = Executors.newFixedThreadPool(200);

        // submit all 1,000 tasks
        List<Future<?>> futures = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            futures.add(executor.submit(() -> {
                List<Integer> numbers = assignment.getNumbers();
                numbers.stream()
                       .filter(n -> n >= 0 && n < 15) // 0-14
                       .forEach(n -> counts[n].incrementAndGet());
            }));
        }

        // Wait for tasks to complete
        futures.forEach(future -> { // Sexy for loop
            try {
                future.get(); // No longer breaks here
            } catch (InterruptedException | ExecutionException e) {
                        System.err.println("Task error: " + e.getMessage());
            }
        });
        executor.shutdown();

        // Build and print results with stream
        String result = IntStream.range(0, 15)
                                 .mapToObj(i -> i + "=" + counts[i].get())
                                 .collect(Collectors.joining(", "));
        System.out.println(result);

        // Counts and prints all tasks after all other processing!!!
        int totalCount = IntStream.range(0, 15) // Creates a stream of Integers
                                  .map(i -> counts[i].get()) // Maps each index into the count value and .get() calls on the AtomicInteger method to retrieve the integers value
                                  .sum(); // Sums up all the Integers in the stream
        System.out.println("Total numbers processed: " + totalCount); // Final print out with sum
    }
}