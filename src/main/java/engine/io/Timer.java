package engine.io;

public class Timer {
    public static double getTime() {
        return (double) System.nanoTime() * 0.000000001;
    }
}
