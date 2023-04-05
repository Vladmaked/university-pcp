package com.company;

public class ArrayMinFinder {
    private final int[] array;
    private int minElement;
    private int minIndex;
    private final int threadCount;
    private int completedWorkers;
    private boolean isWorkersCompleted;
    private final Object lock;


    public ArrayMinFinder(int[] array, int threadCount) {
        this.array = array;
        this.minElement = Integer.MAX_VALUE;
        this.minIndex = -1;
        this.threadCount = threadCount;
        this.completedWorkers = 0;
        this.isWorkersCompleted = false;
        this.lock = new Object();
    }

    public synchronized void updateMin(int min, int index) {
        if (min < minElement) {
            minElement = min;
            minIndex = index;
            notifyAll();
        }
    }

    public synchronized int getMinElement() {
        return minElement;
    }

    public synchronized int getMinIndex() {
        return minIndex;
    }

    public void findMin() {
        int partSize = (array.length + threadCount - 1) / threadCount;
        for (int i = 0; i < threadCount; i++) {
            int start = i * partSize;
            int end = Math.min(start + partSize, array.length);
            Thread worker = new Thread(new ArrayMinWorker(this, array, start, end));
            worker.start();
        }
    }

    public void waitForWorkers() throws InterruptedException {
        synchronized (lock) {
            while (!isWorkersCompleted) {
                lock.wait();
            }
        }
    }

    public void workerCompleted() {
        synchronized (lock) {
            completedWorkers++;
            if (completedWorkers == threadCount) {
                isWorkersCompleted = true;
                lock.notifyAll();
            }
        }
    }
}

