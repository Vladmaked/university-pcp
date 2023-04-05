package com.company;

public class ArrayMinWorker implements Runnable {
    private final ArrayMinFinder finder;
    private final int[] array;
    private final int start;
    private final int end;

    public ArrayMinWorker(ArrayMinFinder finder, int[] array, int start, int end) {
        this.finder = finder;
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    public void run() {
        int min = array[start];
        int minIndex = start;
        for (int i = start + 1; i < end; i++) {
            if (array[i] < min) {
                min = array[i];
                minIndex = i;
            }
        }
        finder.updateMin(min, minIndex);
        finder.workerCompleted();
    }
}
