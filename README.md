# Concurrent Number Processor

A Java application that processes 1 million numbers concurrently from a file, counts occurrences, and prints results.

## Overview

This system demonstrates:
- Multi-threaded batch processing
- Thread-safe aggregation using `ConcurrentHashMap` and `AtomicInteger`
- Simulated API latency handling

## Components

### `Assignment8Application`
- Entry point
- Creates service, triggers processing, and shuts down

### `Assignment8Service`
Core processing logic:
1. Manages thread pool (200 threads)
2. Coordinates concurrent tasks
3. Aggregates results
4. Prints sorted output

### `Assignment8`
Data access layer:
- Reads `output.txt` (1M numbers)
- Provides numbers in batches of 1,000
- Simulates network latency (500ms delay)

## How It Works

1. **Initialization**:
    - Loads 1,000,000 numbers from `output.txt`

2. **Processing**:
    - Creates 1,000 tasks (1 per batch)
    - Each thread:
        - Fetches 1,000 numbers
        - Counts occurrences
    - Thread-safe aggregation via `ConcurrentHashMap`

3. **Output**:
    - Prints each number and its count (sorted)
    - Shows total processed count (should be 1,000,000)

## Requirements

- Java 8+
- `output.txt` in project root containing 1M integers

## Performance Notes

- Processes all data in ~3 seconds (500ms × 1000 batches / 200 threads)
- Thread-safe design prevents race conditions