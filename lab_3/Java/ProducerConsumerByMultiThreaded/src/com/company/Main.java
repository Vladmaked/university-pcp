package com.company;

public class Main {

    public static void main(String[] args) {
        Main main = new Main();
        int storageSize = 4;
        int itemNumbers = 4;
        int producersCount = 3;
        int consumersCount = 4;
        main.starter(storageSize, itemNumbers, producersCount, consumersCount);
    }

    private void starter(int storageSize, int itemNumbers, int producersCount, int consumersCount) {
        Manager manager = new Manager(storageSize);

        for (int i = 0; i < consumersCount; i++) {
            new Consumer(itemNumbers, manager, "Consumer " + (i + 1));
        }

        for (int i = 0; i < producersCount; i++) {
            new Producer(itemNumbers, manager, "Producer " + (i + 1));
        }
    }

}
