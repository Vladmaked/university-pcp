package com.company;

import java.util.concurrent.Semaphore;

public class Philosopher extends Thread {
    private final int id;
    private final Semaphore leftFork;
    private final Semaphore rightFork;
    private final Semaphore forkLimiter;

    public Philosopher(int id, Semaphore leftFork, Semaphore rightFork, Semaphore forkLimiter) {
        this.id = id;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.forkLimiter = forkLimiter;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {
            System.out.println("Philosopher " + id + " thinking " + i + " time");

            try {
                forkLimiter.acquire();
                System.out.println("Philosopher " + id + " allowed to take forks");

                leftFork.acquire();
                System.out.println("Philosopher " + id + " took left fork");

                rightFork.acquire();
                System.out.println("Philosopher " + id + " took right fork");

                System.out.println("Philosopher " + id + " eating " + i + " time");

                rightFork.release();
                System.out.println("Philosopher " + id + " put right fork");

                leftFork.release();
                System.out.println("Philosopher " + id + " put left fork");

                forkLimiter.release();
                System.out.println("Philosopher " + id + " no longer needs forks");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}