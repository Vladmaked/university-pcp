import java.util.Locale;

public class MainThread extends Thread {
    private final int step;
    private final BreakThread breakThread;

    public MainThread(int step, BreakThread breakThread) {
        this.step = step;
        this.breakThread = breakThread;
    }

    @Override
    public void run() {
        long sum = 0;
        while (!breakThread.isCanBreak()) {
            sum += step;
        }
        String message = String.format(Locale.US, "У потоці %d - сума %,d доданків із кроком %d дорівнює сумі %,d", getId(), sum / step, step, sum);
        System.out.println(message);
    }
}