using System;
using System.Threading;
using System.Diagnostics;

namespace ArrayMinFinder
{
    class Program
    {
        private const int arraySize = 1000000;
        private const int randomBound = 100;
        private const int maxThreads = 8;
        private const int iterationsPerThreadCount = 15;

        static void Main(string[] args)
        {
            int[] array = GenerateArray();
            int bestThreadCount = 1;
            double bestAverageTime = double.MaxValue;

            Console.WriteLine("Знаходимо найкращу кількість потоків:");


            for (int threadCount = 1; threadCount <= maxThreads; threadCount++)
            {
                double averageTime = 0;

                for (int iteration = 0; iteration < iterationsPerThreadCount; iteration++)
                {
                    Stopwatch stopwatch = Stopwatch.StartNew();

                    ArrayMinFinder _finder = new ArrayMinFinder(array, threadCount);
                    _finder.FindMin();
                    _finder.WaitForWorkers();

                    stopwatch.Stop();

                    double elapsedTimeInSeconds = (double)stopwatch.ElapsedMilliseconds / 1000;
                    averageTime += elapsedTimeInSeconds;
                }

                averageTime /= iterationsPerThreadCount;
                Console.WriteLine($"Середній час виконання для {threadCount} потоків: {averageTime} секунд");

                if (averageTime < bestAverageTime)
                {
                    bestAverageTime = averageTime;
                    bestThreadCount = threadCount;
                }
            }

            Console.WriteLine($"Найкраща кількість потоків: {bestThreadCount}");

            ArrayMinFinder finder = new ArrayMinFinder(array, bestThreadCount);
            finder.FindMin();
            finder.WaitForWorkers();

            Console.WriteLine($"Мінімальний елемент: {finder.GetMinElement()} на індексі {finder.GetMinIndex()}");
            Console.ReadKey();
        }

        private static int[] GenerateArray()
        {
            Random random = new Random();
            int[] array = new int[arraySize];
            for (int i = 0; i < arraySize; i++)
            {
                array[i] = random.Next(randomBound);
            }

            int randomIndex = random.Next(arraySize);
            int randomNegativeNumber = -random.Next(randomBound);
            array[randomIndex] = randomNegativeNumber;

            return array;
        }
    }

    class ArrayMinFinder
    {
        private readonly int[] array;
        private int minElement;
        private int minIndex;
        private readonly int threadCount;
        private int completedWorkers;
        private bool isWorkersCompleted;
        private readonly object lockObj = new();


        public ArrayMinFinder(int[] array, int threadCount)
        {
            this.array = array;
            this.minElement = int.MaxValue;
            this.minIndex = -1;
            this.threadCount = threadCount;
            this.completedWorkers = 0;
            this.isWorkersCompleted = false;
        }

        public void UpdateMin(int min, int index)
        {
            lock (lockObj)
            {
                if (min < minElement)
                {
                    minElement = min;
                    minIndex = index;
                    Monitor.PulseAll(lockObj);
                }
            }
        }

        public int GetMinElement()
        {
            lock (lockObj)
            {
                return minElement;
            }
        }

        public int GetMinIndex()
        {
            lock (lockObj)
            {
                return minIndex;
            }
        }

        public void FindMin()
        {
            int partSize = (array.Length + threadCount - 1) / threadCount;
            for (int i = 0; i < threadCount; i++)
            {
                int start = i * partSize;
                int end = Math.Min(start + partSize, array.Length);
                Thread worker = new Thread(() => new ArrayMinWorker(this, array, start, end).Run());
                worker.Start();
            }
        }

        public void WaitForWorkers()
        {
            lock (lockObj)
            {
                while (!isWorkersCompleted)
                {
                    Monitor.Wait(lockObj);
                }
            }
        }

        public void WorkerCompleted()
        {
            lock (lockObj)
            {
                completedWorkers++;
                if (completedWorkers == threadCount)
                {
                    isWorkersCompleted = true;
                    Monitor.PulseAll(lockObj);
                }
            }
        }
    }

    class ArrayMinWorker
    {
        private readonly ArrayMinFinder finder;
        private readonly int[] array;
        private readonly int start;
        private readonly int end;

        public ArrayMinWorker(ArrayMinFinder finder, int[] array, int start, int end)
        {
            this.finder = finder;
            this.array = array;
            this.start = start;
            this.end = end;
        }

        public void Run()
        {
            int min = array[start];
            int minIndex = start;
            for (int i = start + 1; i < end; i++)
            {
                if (array[i] < min)
                {
                    min = array[i];
                    minIndex = i;
                }
            }
            finder.UpdateMin(min, minIndex);
            finder.WorkerCompleted();
        }
    }
}
