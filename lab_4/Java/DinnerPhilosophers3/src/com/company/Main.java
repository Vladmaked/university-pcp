package com.company;

import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) {
        int numOfPhilosophers = 5;

        Semaphore[] forks = new Semaphore[numOfPhilosophers];

        for (int i = 0; i < numOfPhilosophers; i++) {
            forks[i] = new Semaphore(1);
        }

        Semaphore forkLimiter = new Semaphore(numOfPhilosophers - 1);

        Philosopher[] philosophers = new Philosopher[numOfPhilosophers];
        for (int i = 0; i < numOfPhilosophers; i++) {
            Semaphore leftFork = forks[i];
            Semaphore rightFork = forks[(i + 1) % numOfPhilosophers];
            philosophers[i] = new Philosopher(i + 1, leftFork, rightFork, forkLimiter);
            philosophers[i].start();
        }
    }
}