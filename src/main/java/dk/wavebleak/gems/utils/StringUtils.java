package dk.wavebleak.gems.utils;

public class StringUtils {

    public static String beautify(String in) {
        String firstLetter = in.substring(0, 1);
        String otherLetters = in.substring(1);
        return firstLetter.toUpperCase() + otherLetters.toLowerCase();
    }

}
