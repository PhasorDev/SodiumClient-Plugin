package Ez.SodiumClient.SRC.utils;

public class MathHelper {
    public MathHelper() {
    }

    public static int clamp(int num, int min, int max) {
        return num < min ? min : Math.min(num, max);
    }

    public static long clamp(long num, long min, long max) {
        return num < min ? min : Math.min(num, max);
    }

    public static float clamp(float num, float min, float max) {
        return num < min ? min : Math.min(num, max);
    }

    public static double clamp(double num, double min, double max) {
        return num < min ? min : Math.min(num, max);
    }

    public static double absMax(double x, double y) {
        if (x < 0.0) {
            x = -x;
        }

        if (y < 0.0) {
            y = -y;
        }

        return Math.max(x, y);
    }
}