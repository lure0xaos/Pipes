package net.michaelkerley;

import java.util.Random;

public final class Rnd {
    private static final Random random = new Random();

    private Rnd() {
    }

    public static int rnd(int max) {
        return Math.abs(random.nextInt()) % max;
    }
}
