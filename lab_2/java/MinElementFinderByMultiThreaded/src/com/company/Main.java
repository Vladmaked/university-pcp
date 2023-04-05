package com.company;

import java.util.Random;

public class Main {
    private static final int arraySize = 1000000;
    private static final int randomBound = 100;
    private static final int maxThreads = 8;
    private static final int iterationsPerThreadCount = 15;

    public static void main(String[] args) throws InterruptedException {
        int[] array = generateArray();
        int bestThreadCount = 1;
        double bestAverageTime = Double.MAX_VALUE;

        System.out.println("Знаходимо найкращу кількість потоків:");

        for (int threadCount = 1; threadCount <= maxThreads; threadCount++) {
            double averageTime = 0;

            for (int iteration = 0; iteration < iterationsPerThreadCount; iteration++) {
                long startTime = System.nanoTime();

                ArrayMinFinder finder = new ArrayMinFinder(array, threadCount);
                finder.findMin();
                finder.waitForWorkers();

                long endTime = System.nanoTime();

                long elapsedTime = endTime - startTime;
                double elapsedTimeInSeconds = (double) elapsedTime / 1_000_000_000;
                averageTime += elapsedTimeInSeconds;
            }

            averageTime /= iterationsPerThreadCount;
            System.out.println("Середній час виконання для " + threadCount + " потоків: " + averageTime + " секунд");

            if (averageTime < bestAverageTime) {
                bestAverageTime = averageTime;
                bestThreadCount = threadCount;
            }
        }

        System.out.println("Найкраща кількість потоків: " + bestThreadCount);

        ArrayMinFinder finder = new ArrayMinFinder(array, bestThreadCount);
        finder.findMin();
        finder.waitForWorkers();

        System.out.println("Мінімальний елемент: " + finder.getMinElement() + " на індексі " + finder.getMinIndex());
    }

    private static int[] generateArray() {
        Random random = new Random();
        int[] array = new int[arraySize];
        for (int i = 0; i < arraySize; i++) {
            array[i] = random.nextInt(randomBound);
        }

        int randomIndex = random.nextInt(arraySize);
        int randomNegativeNumber = -random.nextInt(randomBound);
        array[randomIndex] = randomNegativeNumber;

        return array;
    }
}