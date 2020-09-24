package com.stefan.spaceinvaders.util;

public class LinearEquation {

    public static float[] mAndQ(float x1, float x2, float y1, float y2) {
        float m, q;
        q = (x1 * y2 - x2 * y1) / (x1 - x2);
        m = (y2 - q) / x2;
        return new float[]{m, q};
    }
}
