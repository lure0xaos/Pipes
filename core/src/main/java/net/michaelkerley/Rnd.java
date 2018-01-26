package net.michaelkerley;

import java.util.Random;

public class Rnd {
    private static final Random random = new Random();

    public static int rnd(int max) {
        return Math.abs(random.nextInt()) % max;
    }
}
