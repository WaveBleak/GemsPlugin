package dk.wavebleak.gems.utils;

public class TickUtils {

    public static float ticksToSeconds(int ticks) {
        float seconds = ticks / 20f;
        return Math.round(seconds * 10) / 10f;
    }

}
