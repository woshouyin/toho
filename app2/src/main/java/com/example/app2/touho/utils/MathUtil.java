package com.example.app2.touho.utils;

public class MathUtil {
    public static float PI = (float) Math.PI;

    public static float distance(float x1, float y1, float x2, float y2){
        return (float) Math.sqrt((x1 - x2)*(x1 - x2) + (y1 - y2)*(y1 - y2));
    }
}
