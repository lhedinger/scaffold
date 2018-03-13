package org.hedinger.scaffold.utils;

import java.util.Random;

public class RandomTreeUtils {
    private static Random random;
    {
        random = new Random();
    }

    public static boolean coinFlip() {
        return new Random().nextBoolean();
    }

}
