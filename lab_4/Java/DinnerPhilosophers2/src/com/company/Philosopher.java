package com.company;

import java.util.concurrent.Semaphore;

public class Philosopher extends Thread {
    private final int id;
    private final Semaphore leftFork;
    private final Semaphore rightFork;
    private final Semaphore token;

    public Philosopher(int id, Semaphore leftFork, Semaphore rightFork, Semaphore token) {
        this.id = id;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.token = token;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {
            System.out.println("Philosopher " + id + " thinking " + i + " time");

            try {
                token.acquire();
                System.out.println("Philosopher " + id + " got token");

                leftFork.acquire();
                System.out.println("Philosopher " + id + " took left fork");

                rightFork.acquire();
                System.out.println("Philosopher " + id + " took right fork");

                System.out.println("Philosopher " + id + " eating " + i + " time");

                rightFork.release();
                System.out.println("Philosopher " + id + " put right fork");

                leftFork.release();
                System.out.println("Philosopher " + id + " put left fork");

                token.release();
                System.out.println("Philosopher " + id + " returned token");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}