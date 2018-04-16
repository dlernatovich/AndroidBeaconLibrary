package com.artlite.beacon.library.managers;

import android.support.annotation.IntRange;

import java.util.Random;

/**
 * Class which provide the random help
 */

final class BCRandomHelper {

    private static final int MIN_VALUE = 0;
    private static final int MAX_VALUE = 900000;
    private static final String ALPHABET = "QWERTYUIOP{}ASDFGHJKL:|ZXCVBNM<>?qwertyuiop[]" +
            "asdfghjkl;'zxcvbnm,./1234567890-=+_";
    private static final String ALPHABET_LETTERS_ONLY = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiop" +
            "asdfghjklzxcvbnm1234567890";

    /**
     * Method which provide the generating of the random integer value
     *
     * @return current random integer value
     */
    public static int generateInt() {
        return generateInt(MIN_VALUE, MAX_VALUE);
    }

    /**
     * Method which provide the generating of the random integer value
     *
     * @param max max value
     * @return current random integer value
     */
    public static int generateInt(int max) {
        return generateInt(MIN_VALUE, max);
    }

    /**
     * Method which provide the generating of the random integer value
     *
     * @param min min value
     * @param max max value
     * @return current random integer value
     */
    public static int generateInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    /**
     * Method which provide the generating of the random String value with length
     *
     * @param length current random string length
     * @return generated String value
     */
    public static String generateString(@IntRange(from = 1, to = Integer.MAX_VALUE) int length,
                                        boolean onlyLetters) {
        char[] chars = (onlyLetters == true) ? ALPHABET_LETTERS_ONLY.toCharArray()
                : ALPHABET.toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String output = sb.toString().trim();
        return output;
    }
}
