using System.Threading;
using System;
using System.Diagnostics.Metrics;

namespace threaddemo
{
    class Program
    {
        private volatile bool canStop = false;
        private readonly object locker = new();

        static void Main() => new Program().Start();

        void Start()
        {
            int numberOfThreads = 200;
            Random random = new();
            for (int i = 0; i < numberOfThreads; i++)
            {
                new Thread(() => Calculator(random.Next(1, 6))).Start();
            }

            new Thread(() => Stopper(30)).Start();
        }

        void Calculator(int step)
        {
            long sum = 0;
            while (true)
            {
                lock (locker)
                {
                    if (canStop)
                        break;
                    sum += step;
                }
            }
            int threadId = Environment.CurrentManagedThreadId;
            Console.WriteLine($"У потоці {threadId} - сума {sum / step:N0} доданків з кроком {step} дорівнює сумі {sum:N0}");
        }

        public void Stopper(int seconds)
        {
            Thread.Sleep(seconds * 1000);
            lock (locker)
            {
                canStop = true;
            }
        }
    }
}
