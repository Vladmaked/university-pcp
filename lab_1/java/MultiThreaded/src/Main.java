import java.util.Arrays;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        int[] steps = new int[200];
        Random random = new Random();
        Arrays.setAll(steps, i -> random.nextInt(5) + 1);

        BreakThread breakThread = new BreakThread(30);

        for (int step : steps) {
            new MainThread(step, breakThread).start();
        }

        new Thread(breakThread).start();
    }
}