public class BreakThread implements Runnable {
    private final long second;
    private volatile boolean isCanBreak = false;
    public BreakThread(int second) {
        this.second = second;
    }

    public boolean isCanBreak() {
        return isCanBreak;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(second * 1000);
            isCanBreak = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        isCanBreak = true;
    }
}