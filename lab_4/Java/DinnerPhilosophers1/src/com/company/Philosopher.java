package com.company;

import java.util.concurrent.Semaphore;

public class Philosopher extends Thread {
    private final int id;
    private final Semaphore leftFork;
    private final Semaphore rightFork;

    public Philosopher(int id, Semaphore leftFork, Semaphore rightFork) {
        this.id = id;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {
            System.out.println("Philosopher " + id + " thinking " + i + " time");

            try {
                leftFork.acquire();
                System.out.println("Philosopher " + id + " took left fork");

                rightFork.acquire();
                System.out.println("Philosopher " + id + " took right fork");

                System.out.println("Philosopher " + id + " eating " + i + " time");

                rightFork.release();
                System.out.println("Philosopher " + id + " put right fork");

                leftFork.release();
                System.out.println("Philosopher " + id + " put left fork");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}