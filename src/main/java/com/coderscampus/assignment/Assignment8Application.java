package com.coderscampus.assignment;

public class Assignment8Application {

    public static void main(String[] args) {

        Assignment8Service service = new Assignment8Service();
        service.performConcurrentCounting(1000);
        service.printResults();
        service.shutdown();
    }
}