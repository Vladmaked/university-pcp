package com.company;

import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) {
        int numOfPhilosophers = 5;

        Semaphore[] forks = new Semaphore[numOfPhilosophers];

        for (int i = 0; i < numOfPhilosophers; i++) {
            forks[i] = new Semaphore(1);
        }

        Semaphore[] tokens = new Semaphore[2];
        for (int i = 0; i < 2; i++) {
            tokens[i] = new Semaphore(1);
        }

        Philosopher[] philosophers = new Philosopher[numOfPhilosophers];
        for (int i = 0; i < numOfPhilosophers; i++) {
            Semaphore leftFork = forks[i];
            Semaphore rightFork = forks[(i + 1) % numOfPhilosophers];
            Semaphore token = tokens[i % 2];
            philosophers[i] = new Philosopher(i + 1, leftFork, rightFork, token);
            philosophers[i].start();
        }
    }
}