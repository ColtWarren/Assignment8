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
        Assignment8 assignment = new Assignment8();
        AtomicInteger[] counts = IntStream.range(0, 15)
                                          .mapToObj(i -> new AtomicInteger(0))
                                          .toArray(AtomicInteger[]::new);

        ExecutorService executor = Executors.newFixedThreadPool(200);
        List<Future<?>> futures = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            futures.add(executor.submit(() -> {
                List<Integer> numbers = assignment.getNumbers();
                numbers.stream()
                       .filter(n -> n >= 0 && n < 15) 
                       .forEach(n -> counts[n].incrementAndGet());
            }));
        }

        futures.forEach(future -> {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                        System.err.println("Error in task: " + e.getMessage());
            }
        });
        executor.shutdown();

        String result = IntStream.range(0, 15)
                                 .mapToObj(i -> i + "=" + counts[i].get())
                                 .collect(Collectors.joining(", "));
        System.out.println(result);

        int totalCount = IntStream.range(0, 15)
                                  .map(i -> counts[i].get())
                                  .sum();
        System.out.println("Total numbers processed: " + totalCount);
    }
}